package com.schedmailer.service.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.domain.enums.EmailStatus;
import com.schedmailer.repository.ScheduledEmailRepository;
import com.schedmailer.service.EmailSenderService;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EmailRetrySchedulerTest {

    @Mock private ScheduledEmailRepository scheduledEmailRepository;

    @Mock private EmailSenderService emailSenderService;

    @InjectMocks private EmailRetryScheduler emailRetryScheduler;

    private ScheduledEmail testEmail;

    @BeforeEach
    void setUp() {
        testEmail =
                ScheduledEmail.builder()
                        .id(UUID.randomUUID())
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .shouldSendAt(ZonedDateTime.now().minusMinutes(1))
                        .build();
    }

    @Test
    void processScheduledEmails_WithNoEmails_ShouldDoNothing() {
        // Given
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(Collections.emptyList());

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(scheduledEmailRepository).findPendingOrRetryingDue(any(ZonedDateTime.class));
        verifyNoInteractions(emailSenderService);
        verify(scheduledEmailRepository, never()).save(any());
    }

    @Test
    void processScheduledEmails_WithSuccessfulSend_ShouldMarkAsSent() throws MessagingException {
        // Given
        List<ScheduledEmail> emails = List.of(testEmail);
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(emails);
        doNothing().when(emailSenderService).send(testEmail);

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(emailSenderService).send(testEmail);
        verify(scheduledEmailRepository).save(testEmail);
        assertEquals(EmailStatus.SENT, testEmail.getStatus());
        assertNotNull(testEmail.getSentAt());
    }

    @Test
    void processScheduledEmails_WithFirstFailure_ShouldScheduleRetry() throws MessagingException {
        // Given
        List<ScheduledEmail> emails = List.of(testEmail);
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(emails);
        doThrow(new RuntimeException("Send failed")).when(emailSenderService).send(testEmail);

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(emailSenderService).send(testEmail);
        verify(scheduledEmailRepository).save(testEmail);
        assertEquals(EmailStatus.RETRYING, testEmail.getStatus());
        assertEquals(1, testEmail.getRetryCount());
        assertTrue(testEmail.getShouldSendAt().isAfter(ZonedDateTime.now()));
    }

    @Test
    void processScheduledEmails_WithMaxRetriesExceeded_ShouldMarkAsFailed()
            throws MessagingException {
        // Given
        testEmail.setRetryCount(5); // Already at max retries
        List<ScheduledEmail> emails = List.of(testEmail);
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(emails);
        doThrow(new RuntimeException("Send failed")).when(emailSenderService).send(testEmail);

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(emailSenderService).send(testEmail);
        verify(scheduledEmailRepository).save(testEmail);
        assertEquals(EmailStatus.FAILED, testEmail.getStatus());
        assertEquals(5, testEmail.getRetryCount());
    }

    @Test
    void processScheduledEmails_WithMultipleEmails_ShouldProcessAll() throws MessagingException {
        // Given
        ScheduledEmail email1 =
                ScheduledEmail.builder()
                        .id(UUID.randomUUID())
                        .toEmail("test1@example.com")
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .build();

        ScheduledEmail email2 =
                ScheduledEmail.builder()
                        .id(UUID.randomUUID())
                        .toEmail("test2@example.com")
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .build();

        List<ScheduledEmail> emails = List.of(email1, email2);
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(emails);
        doNothing().when(emailSenderService).send(any(ScheduledEmail.class));

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(emailSenderService, times(2)).send(any(ScheduledEmail.class));
        verify(scheduledEmailRepository, times(2)).save(any(ScheduledEmail.class));
        assertEquals(EmailStatus.SENT, email1.getStatus());
        assertEquals(EmailStatus.SENT, email2.getStatus());
    }

    @Test
    void processScheduledEmails_WithBackoffCalculation_ShouldIncreaseDelay()
            throws MessagingException {
        // Given
        testEmail.setRetryCount(2); // Second attempt
        List<ScheduledEmail> emails = List.of(testEmail);
        when(scheduledEmailRepository.findPendingOrRetryingDue(any(ZonedDateTime.class)))
                .thenReturn(emails);
        doThrow(new RuntimeException("Send failed")).when(emailSenderService).send(testEmail);

        ZonedDateTime beforeProcessing = ZonedDateTime.now();

        // When
        emailRetryScheduler.processScheduledEmails();

        // Then
        verify(scheduledEmailRepository).save(testEmail);
        assertEquals(EmailStatus.RETRYING, testEmail.getStatus());
        assertEquals(3, testEmail.getRetryCount());

        // Should be scheduled for 2^3 * 60 = 480 seconds later
        long expectedDelaySeconds = (long) (Math.pow(2, 3) * 60);
        ZonedDateTime expectedTime = beforeProcessing.plusSeconds(expectedDelaySeconds);
        assertTrue(testEmail.getShouldSendAt().isAfter(expectedTime.minusSeconds(5)));
        assertTrue(testEmail.getShouldSendAt().isBefore(expectedTime.plusSeconds(5)));
    }
}
