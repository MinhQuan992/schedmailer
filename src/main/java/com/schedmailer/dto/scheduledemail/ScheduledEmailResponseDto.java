package com.schedmailer.dto.scheduledemail;

import com.schedmailer.domain.enums.EmailStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for scheduled email information")
public class ScheduledEmailResponseDto {
    @Schema(
            description = "Unique identifier of the scheduled email",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Recipient email address", example = "recipient@example.com")
    private String toEmail;

    @Schema(description = "Email subject line", example = "Important Notification")
    private String subject;

    @Schema(description = "Email body content", example = "This is the email content.")
    private String body;

    @Schema(description = "Current status of the scheduled email", example = "PENDING")
    private EmailStatus status;

    @Schema(
            description = "Scheduled date and time when the email should be sent",
            example = "2024-12-25T10:00:00Z")
    private ZonedDateTime shouldSendAt;

    @Schema(
            description = "Date and time when the email was actually sent",
            example = "2024-12-25T10:00:05Z")
    private ZonedDateTime sentAt;

    @Schema(description = "Number of retry attempts made to send the email", example = "0")
    private int retryCount;

    @Schema(
            description = "ID of the SMTP configuration used for sending",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID smtpConfigId;
}
