package com.schedmailer.controller;

import com.schedmailer.dto.smtpconfig.SmtpConfigCreateDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import com.schedmailer.service.SmtpConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.schedmailer.common.constants.EndpointConfig.ENDPOINT_V1;
import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG;
import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG_BY_ID;

@RestController
@RequestMapping(ENDPOINT_V1 + SMTP_CONFIG)
@RequiredArgsConstructor
public class SmtpConfigController {
    private final SmtpConfigService smtpConfigService;

    @PostMapping
    public ResponseEntity<SmtpConfigDto> createSmtpConfig(
            @RequestBody @Valid SmtpConfigCreateDto smtpConfigCreateDto, UriComponentsBuilder ucb) {
        SmtpConfigDto newSmtpConfig = smtpConfigService.createSmtpConfig(smtpConfigCreateDto);
        URI newSmtpConfigId =
                ucb.path(SMTP_CONFIG_BY_ID).buildAndExpand(newSmtpConfig.getId()).toUri();
        return ResponseEntity.created(newSmtpConfigId).build();
    }
}
