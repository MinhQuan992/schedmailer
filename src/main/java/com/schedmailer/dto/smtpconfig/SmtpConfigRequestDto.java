package com.schedmailer.dto.smtpconfig;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;

@Builder
public record SmtpConfigRequestDto(
        @Schema(description = "SMTP server hostname", example = "smtp.gmail.com")
                @NotBlank(message = "Host is required")
                String host,
        @Schema(description = "SMTP server port", example = "587")
                @NotBlank(message = "Port is required")
                @Pattern(regexp = "^[0-9]+$", message = "Port must be a number")
                String port,
        @Schema(description = "SMTP server username", example = "user@gmail.com")
                @NotBlank(message = "Username is required")
                String username,
        @Schema(
                        description = "SMTP server password (optional for updates)",
                        example = "password123")
                String password,
        @Schema(description = "Email address to send from", example = "noreply@example.com")
                @Email(message = "From email must be valid")
                String fromEmail,
        @Schema(description = "Whether to use SSL", example = "true") Boolean useSsl,
        @Schema(description = "Whether to use TLS", example = "true") Boolean useTls) {}
