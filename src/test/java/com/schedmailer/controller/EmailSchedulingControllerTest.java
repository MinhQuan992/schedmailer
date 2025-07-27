package com.schedmailer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.schedmailer.domain.enums.EmailStatus;
import com.schedmailer.dto.scheduledemail.ScheduledEmailRequestDto;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;
import com.schedmailer.service.EmailSchedulingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EmailSchedulingControllerTest {

    @Mock private EmailSchedulingService emailSchedulingService;

    @InjectMocks private EmailSchedulingController emailSchedulingController;

    private ScheduledEmailRequestDto requestDto;
    private ScheduledEmailResponseDto responseDto;

    @BeforeEach
    void setUp() {
        ZonedDateTime scheduledTime = ZonedDateTime.now().plusHours(1);
        UUID emailId = UUID.randomUUID();
        UUID smtpConfigId = UUID.randomUUID();

        requestDto =
                ScheduledEmailRequestDto.builder()
                        .toEmail("test@example.com")
                        .subject("Test Subject")
                        .body("Test Body")
                        .shouldSendAt(scheduledTime)
                        .smtpConfigId(smtpConfigId)
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
    void scheduleEmail_WithValidRequest_ShouldReturnOkResponse() {
        // Given
        when(emailSchedulingService.scheduleEmail(requestDto)).thenReturn(responseDto);

        // When
        ResponseEntity<ScheduledEmailResponseDto> response =
                emailSchedulingController.scheduleEmail(requestDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getId(), response.getBody().getId());
        assertEquals(responseDto.getToEmail(), response.getBody().getToEmail());
        assertEquals(responseDto.getSubject(), response.getBody().getSubject());
        assertEquals(responseDto.getBody(), response.getBody().getBody());
        assertEquals(responseDto.getStatus(), response.getBody().getStatus());

        verify(emailSchedulingService).scheduleEmail(requestDto);
    }
}
