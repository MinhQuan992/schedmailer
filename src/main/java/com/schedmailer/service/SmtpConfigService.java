package com.schedmailer.service;

import com.schedmailer.dto.smtpconfig.SmtpConfigRequestDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;
import com.schedmailer.entity.SmtpConfig;
import com.schedmailer.exception.ResourceNotFoundException;
import com.schedmailer.mapper.SmtpConfigMapper;
import com.schedmailer.repository.SmtpConfigRepository;
import com.schedmailer.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SmtpConfigService {
    private final SmtpConfigRepository smtpConfigRepository;
    private final SmtpConfigMapper smtpConfigMapper;

    @Value("${encryption.key}")
    private String encryptionKey;

    public SmtpConfigResponseDto createSmtpConfig(SmtpConfigRequestDto smtpConfigRequestDto) {
        String encryptedPassword =
                EncryptionUtil.encrypt(smtpConfigRequestDto.password(), encryptionKey);

        SmtpConfig smtpConfig =
                SmtpConfig.builder()
                        .host(smtpConfigRequestDto.host())
                        .port(Integer.parseInt(smtpConfigRequestDto.port()))
                        .username(smtpConfigRequestDto.username())
                        .password(encryptedPassword)
                        .fromEmail(smtpConfigRequestDto.fromEmail())
                        .useSsl(
                                smtpConfigRequestDto.useSsl() != null
                                        && smtpConfigRequestDto.useSsl())
                        .useTls(
                                smtpConfigRequestDto.useTls() != null
                                        && smtpConfigRequestDto.useTls())
                        .build();
        SmtpConfig newSmtpConfig = smtpConfigRepository.save(smtpConfig);

        return smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(newSmtpConfig);
    }

    public List<SmtpConfigResponseDto> getAllSmtpConfigs() {
        List<SmtpConfig> smtpConfigs = smtpConfigRepository.findAll();
        return smtpConfigs.stream()
                .map(smtpConfigMapper::smtpConfigToSmtpConfigResponseDto)
                .collect(Collectors.toList());
    }

    public SmtpConfigResponseDto getSmtpConfigById(String id) {
        SmtpConfig smtpConfig = getSmtpConfigFromDbById(id);
        return smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(smtpConfig);
    }

    public SmtpConfigResponseDto updateSmtpConfig(
            String id, SmtpConfigRequestDto smtpConfigRequestDto) {
        SmtpConfig existingSmtpConfig = getSmtpConfigFromDbById(id);

        existingSmtpConfig.setHost(smtpConfigRequestDto.host());
        existingSmtpConfig.setPort(Integer.parseInt(smtpConfigRequestDto.port()));
        existingSmtpConfig.setUsername(smtpConfigRequestDto.username());

        if (smtpConfigRequestDto.password() != null && !smtpConfigRequestDto.password().isEmpty()) {
            String encryptedPassword =
                    EncryptionUtil.encrypt(smtpConfigRequestDto.password(), encryptionKey);
            existingSmtpConfig.setPassword(encryptedPassword);
        }

        existingSmtpConfig.setFromEmail(smtpConfigRequestDto.fromEmail());
        existingSmtpConfig.setUseSsl(
                smtpConfigRequestDto.useSsl() != null && smtpConfigRequestDto.useSsl());
        existingSmtpConfig.setUseTls(
                smtpConfigRequestDto.useTls() != null && smtpConfigRequestDto.useTls());

        SmtpConfig updatedSmtpConfig = smtpConfigRepository.save(existingSmtpConfig);
        return smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(updatedSmtpConfig);
    }

    public void deleteSmtpConfig(String id) {
        getSmtpConfigFromDbById(id);
        smtpConfigRepository.deleteById(UUID.fromString(id));
    }

    private SmtpConfig getSmtpConfigFromDbById(String id) {
        return smtpConfigRepository
                .findById(UUID.fromString(id))
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "SMTP Configuration not found with ID: " + id));
    }
}
