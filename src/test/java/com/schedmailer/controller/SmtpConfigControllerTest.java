package com.schedmailer.controller;

import com.schedmailer.dto.smtpconfig.SmtpConfigCreateDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import com.schedmailer.service.SmtpConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG_BY_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SmtpConfigControllerTest {
    @Mock private SmtpConfigService smtpConfigService;

    @InjectMocks private SmtpConfigController smtpConfigController;

    private SmtpConfigCreateDto createDto;
    private SmtpConfigDto resultDto;
    private UriComponentsBuilder uriBuilder;
    private UUID configId;

    @BeforeEach
    void setUp() {
        configId = UUID.randomUUID();
        createDto = SmtpConfigCreateDto.builder().build();
        resultDto = new SmtpConfigDto();
        resultDto.setId(configId);
        uriBuilder = UriComponentsBuilder.newInstance().path("/api");
    }

    @Test
    void createSmtpConfig_WithValidInput_ReturnsCreatedStatus() {
        // Arrange
        when(smtpConfigService.createSmtpConfig(any(SmtpConfigCreateDto.class)))
                .thenReturn(resultDto);

        // Act
        ResponseEntity<SmtpConfigDto> response =
                smtpConfigController.createSmtpConfig(createDto, uriBuilder);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(smtpConfigService).createSmtpConfig(createDto);
    }

    @Test
    void createSmtpConfig_WithValidInput_GeneratesCorrectLocationHeader() {
        // Arrange
        when(smtpConfigService.createSmtpConfig(any(SmtpConfigCreateDto.class)))
                .thenReturn(resultDto);
        URI expectedUri =
                URI.create("/api" + SMTP_CONFIG_BY_ID.replace("{id}", configId.toString()));

        // Act
        ResponseEntity<SmtpConfigDto> response =
                smtpConfigController.createSmtpConfig(createDto, uriBuilder);

        // Assert
        assertEquals(expectedUri, response.getHeaders().getLocation());
        assertTrue(
                Objects.requireNonNull(response.getHeaders().getLocation())
                        .toString()
                        .endsWith(configId.toString()));
    }
}
