package com.schedmailer.service;

import com.schedmailer.dto.smtpconfig.SmtpConfigCreateDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import com.schedmailer.entity.SmtpConfig;
import com.schedmailer.mapper.SmtpConfigMapper;
import com.schedmailer.repository.SmtpConfigRepository;
import com.schedmailer.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpConfigService {
    private final SmtpConfigRepository smtpConfigRepository;

    @Value("${encryption.key}")
    private String encryptionKey;

    public SmtpConfigDto createSmtpConfig(SmtpConfigCreateDto smtpConfigCreateDto) {
        String encryptedPassword =
                EncryptionUtil.encrypt(smtpConfigCreateDto.password(), encryptionKey);

        SmtpConfig smtpConfig =
                SmtpConfig.builder()
                        .host(smtpConfigCreateDto.host())
                        .port(Integer.parseInt(smtpConfigCreateDto.port()))
                        .username(smtpConfigCreateDto.username())
                        .password(encryptedPassword)
                        .fromEmail(smtpConfigCreateDto.fromEmail())
                        .useSsl(
                                smtpConfigCreateDto.useSsl() != null
                                        && smtpConfigCreateDto.useSsl())
                        .useTls(
                                smtpConfigCreateDto.useTls() != null
                                        && smtpConfigCreateDto.useTls())
                        .build();
        SmtpConfig newSmtpConfig = smtpConfigRepository.save(smtpConfig);

        return SmtpConfigMapper.INSTANCE.smtpConfigToSmtpConfigDto(newSmtpConfig);
    }
}
