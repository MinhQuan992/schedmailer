package com.schedmailer.dto.smtpconfig;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SMTP Configuration Response Data")
public class SmtpConfigResponseDto {
    @Schema(
            description = "Unique identifier of the SMTP configuration",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "SMTP server hostname", example = "smtp.gmail.com")
    private String host;

    @Schema(description = "SMTP server port", example = "587")
    private int port;

    @Schema(description = "SMTP server username/login", example = "user@example.com")
    private String username;

    @Schema(description = "Email address used as the sender", example = "noreply@example.com")
    private String fromEmail;

    @Schema(description = "Whether SSL encryption is enabled", example = "true")
    private boolean useSsl;

    @Schema(description = "Whether TLS encryption is enabled", example = "true")
    private boolean useTls;
}
