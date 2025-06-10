package com.schedmailer.dto.smtpconfig;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SmtpConfigCreateDto(
        @NotBlank(message = "SMTP host cannot be blank") String host,
        @NotBlank(message = "SMTP port cannot be blank") String port,
        @NotBlank(message = "SMTP username cannot be blank") String username,
        @NotBlank(message = "SMTP password cannot be blank") String password,
        String fromEmail,
        Boolean useSsl,
        Boolean useTls) {}
