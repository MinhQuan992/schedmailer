package com.schedmailer.dto.smtpconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SmtpConfigDto {
    private UUID id;
    private String host;
    private int port;
    private String username;
    private String fromEmail;
    private boolean useSsl;
    private boolean useTls;
}
