package com.schedmailer.controller;

import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG_BY_ID;

import com.schedmailer.api.SmtpConfigApi;
import com.schedmailer.dto.smtpconfig.SmtpConfigRequestDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;
import com.schedmailer.service.SmtpConfigService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SmtpConfigController implements SmtpConfigApi {
    private final SmtpConfigService smtpConfigService;

    @Override
    public ResponseEntity<SmtpConfigResponseDto> createSmtpConfig(
            SmtpConfigRequestDto smtpConfigRequestDto, UriComponentsBuilder ucb) {
        SmtpConfigResponseDto newSmtpConfig =
                smtpConfigService.createSmtpConfig(smtpConfigRequestDto);
        URI newSmtpConfigId =
                ucb.path(SMTP_CONFIG_BY_ID).buildAndExpand(newSmtpConfig.getId()).toUri();
        return ResponseEntity.created(newSmtpConfigId).build();
    }

    @Override
    public ResponseEntity<List<SmtpConfigResponseDto>> getAllSmtpConfigs() {
        List<SmtpConfigResponseDto> smtpConfigs = smtpConfigService.getAllSmtpConfigs();
        return ResponseEntity.ok(smtpConfigs);
    }

    @Override
    public ResponseEntity<SmtpConfigResponseDto> getSmtpConfigById(String id) {
        SmtpConfigResponseDto smtpConfig = smtpConfigService.getSmtpConfigById(id);
        return ResponseEntity.ok(smtpConfig);
    }

    @Override
    public ResponseEntity<SmtpConfigResponseDto> updateSmtpConfig(
            String id, SmtpConfigRequestDto smtpConfigRequestDto) {
        SmtpConfigResponseDto updatedSmtpConfig =
                smtpConfigService.updateSmtpConfig(id, smtpConfigRequestDto);
        return ResponseEntity.ok(updatedSmtpConfig);
    }

    @Override
    public ResponseEntity<Void> deleteSmtpConfig(String id) {
        smtpConfigService.deleteSmtpConfig(id);
        return ResponseEntity.noContent().build();
    }
}
