package com.schedmailer.controller;

import com.schedmailer.api.SmtpConfigApi;
import com.schedmailer.dto.smtpconfig.SmtpConfigCreateDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import com.schedmailer.service.SmtpConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG_BY_ID;

@RestController
@RequiredArgsConstructor
public class SmtpConfigController implements SmtpConfigApi {
    private final SmtpConfigService smtpConfigService;

    @Override
    public ResponseEntity<SmtpConfigDto> createSmtpConfig(
            SmtpConfigCreateDto smtpConfigCreateDto, UriComponentsBuilder ucb) {
        SmtpConfigDto newSmtpConfig = smtpConfigService.createSmtpConfig(smtpConfigCreateDto);
        URI newSmtpConfigId =
                ucb.path(SMTP_CONFIG_BY_ID).buildAndExpand(newSmtpConfig.getId()).toUri();
        return ResponseEntity.created(newSmtpConfigId).build();
    }
}
