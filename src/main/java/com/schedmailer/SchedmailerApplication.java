package com.schedmailer;

import com.schedmailer.config.EncryptionKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({EncryptionKeyProperties.class})
public class SchedmailerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedmailerApplication.class, args);
    }
}
