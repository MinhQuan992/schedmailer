package com.schedmailer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "encryption")
@Getter
@Setter
public class EncryptionKeyProperties {
    /**
     * The encryption key used for securing sensitive data. Can be set via the ENCRYPTION_KEY
     * environment variable.
     */
    private String key;
}
