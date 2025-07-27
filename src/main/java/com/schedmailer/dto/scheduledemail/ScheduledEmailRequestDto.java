package com.schedmailer.dto.scheduledemail;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record ScheduledEmailRequestDto(
        @Schema(description = "Recipient email address", example = "recipient@example.com")
                @NotBlank(message = "To email is required")
                @Email(message = "To email must be valid")
                String toEmail,
        @Schema(description = "Email subject line", example = "Important Notification")
                @NotBlank(message = "Subject is required")
                String subject,
        @Schema(description = "Email body content", example = "This is the email content.")
                @NotBlank(message = "Body is required")
                String body,
        @Schema(
                        description =
                                "Scheduled date and time to send the email. If null, the email will be sent immediately. If provided, must be in the future.",
                        example = "2024-12-25T10:00:00Z",
                        nullable = true)
                @Future(message = "Scheduled send time must be in the future")
                ZonedDateTime shouldSendAt,
        @Schema(
                        description = "ID of the SMTP configuration to use for sending",
                        example = "123e4567-e89b-12d3-a456-426614174000")
                @NotNull(message = "SMTP configuration ID is required")
                UUID smtpConfigId) {}
