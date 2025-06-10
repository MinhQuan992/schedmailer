package com.schedmailer;

import com.schedmailer.config.EncryptionKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({EncryptionKeyProperties.class})
public class SchedmailerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedmailerApplication.class, args);
    }
}
