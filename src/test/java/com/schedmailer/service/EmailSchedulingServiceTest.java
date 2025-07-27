package com.schedmailer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.domain.entity.SmtpConfig;
import com.schedmailer.domain.enums.EmailStatus;
import com.schedmailer.dto.scheduledemail.ScheduledEmailRequestDto;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;
import com.schedmailer.exception.ResourceNotFoundException;
import com.schedmailer.mapper.ScheduledEmailMapper;
import com.schedmailer.repository.ScheduledEmailRepository;
import com.schedmailer.repository.SmtpConfigRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EmailSchedulingServiceTest {

    @Mock private ScheduledEmailRepository scheduledEmailRepository;

    @Mock private SmtpConfigRepository smtpConfigRepository;

    @Mock private ScheduledEmailMapper scheduledEmailMapper;

    @InjectMocks private EmailSchedulingService emailSchedulingService;

    private ScheduledEmailRequestDto requestDto;
    private SmtpConfig smtpConfig;
    private ScheduledEmail scheduledEmail;
    private ScheduledEmailResponseDto responseDto;
    private UUID smtpConfigId;

    @BeforeEach
    void setUp() {
        ZonedDateTime scheduledTime = ZonedDateTime.now().plusHours(1);
        smtpConfigId = UUID.randomUUID();
        UUID emailId = UUID.randomUUID();

        requestDto =
                ScheduledEmailRequestDto.builder()
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .shouldSendAt(scheduledTime)
                        .smtpConfigId(smtpConfigId)
                        .build();

        smtpConfig =
                SmtpConfig.builder()
                        .id(smtpConfigId)
                        .host("smtp.example.com")
                        .port(587)
                        .username("user@example.com")
                        .password("password")
                        .build();

        scheduledEmail =
                ScheduledEmail.builder()
                        .id(emailId)
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .shouldSendAt(scheduledTime)
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .smtpConfig(smtpConfig)
                        .build();

        responseDto =
                ScheduledEmailResponseDto.builder()
                        .id(emailId)
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .shouldSendAt(scheduledTime)
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .sentAt(null)
                        .smtpConfigId(smtpConfigId)
                        .build();
    }

    @Test
    void scheduleEmail_WithValidRequest_ShouldReturnScheduledEmail() {
        // Given
        when(smtpConfigRepository.findById(smtpConfigId)).thenReturn(Optional.of(smtpConfig));
        when(scheduledEmailRepository.save(any(ScheduledEmail.class))).thenReturn(scheduledEmail);
        when(scheduledEmailMapper.scheduledEmailToScheduledEmailResponseDto(scheduledEmail))
                .thenReturn(responseDto);

        // When
        ScheduledEmailResponseDto result = emailSchedulingService.scheduleEmail(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getToEmail(), result.getToEmail());
        assertEquals(responseDto.getSubject(), result.getSubject());
        assertEquals(responseDto.getBody(), result.getBody());
        assertEquals(responseDto.getStatus(), result.getStatus());
        assertEquals(responseDto.getRetryCount(), result.getRetryCount());

        verify(smtpConfigRepository).findById(smtpConfigId);
        verify(scheduledEmailRepository).save(any(ScheduledEmail.class));
        verify(scheduledEmailMapper).scheduledEmailToScheduledEmailResponseDto(scheduledEmail);
    }

    @Test
    void scheduleEmail_WithInvalidSmtpConfigId_ShouldThrowResourceNotFoundException() {
        // Given
        when(smtpConfigRepository.findById(smtpConfigId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> emailSchedulingService.scheduleEmail(requestDto));

        assertEquals("SMTP Config not found", exception.getMessage());
        verify(smtpConfigRepository).findById(smtpConfigId);
        verifyNoInteractions(scheduledEmailRepository);
        verifyNoInteractions(scheduledEmailMapper);
    }

    @Test
    void scheduleEmail_ShouldCreateEmailWithCorrectProperties() {
        // Given
        when(smtpConfigRepository.findById(smtpConfigId)).thenReturn(Optional.of(smtpConfig));
        when(scheduledEmailRepository.save(any(ScheduledEmail.class))).thenReturn(scheduledEmail);
        when(scheduledEmailMapper.scheduledEmailToScheduledEmailResponseDto(
                        any(ScheduledEmail.class)))
                .thenReturn(responseDto);

        // When
        emailSchedulingService.scheduleEmail(requestDto);

        // Then
        verify(scheduledEmailRepository)
                .save(
                        argThat(
                                email ->
                                        email.getToEmail().equals("test@example.com")
                                                && email.getSubject().equals("Test Subject")
                                                && email.getBody().equals("Test Body")
                                                && email.getStatus() == EmailStatus.PENDING
                                                && email.getRetryCount() == 0
                                                && email.getSmtpConfig().equals(smtpConfig)));
    }

    @Test
    void scheduleEmail_ShouldSetCorrectScheduledTime() {
        // Given
        ZonedDateTime futureTime = ZonedDateTime.now().plusDays(1);
        ScheduledEmailRequestDto futureRequest =
                ScheduledEmailRequestDto.builder()
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .shouldSendAt(futureTime)
                        .smtpConfigId(smtpConfigId)
                        .build();

        when(smtpConfigRepository.findById(smtpConfigId)).thenReturn(Optional.of(smtpConfig));
        when(scheduledEmailRepository.save(any(ScheduledEmail.class))).thenReturn(scheduledEmail);
        when(scheduledEmailMapper.scheduledEmailToScheduledEmailResponseDto(
                        any(ScheduledEmail.class)))
                .thenReturn(responseDto);

        // When
        emailSchedulingService.scheduleEmail(futureRequest);

        // Then
        verify(scheduledEmailRepository)
                .save(argThat(email -> email.getShouldSendAt().equals(futureTime)));
    }
}
