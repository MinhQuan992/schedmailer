package com.schedmailer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.schedmailer.dto.smtpconfig.SmtpConfigRequestDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;
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

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SmtpConfigControllerTest {

    @Mock private SmtpConfigService smtpConfigService;

    @InjectMocks private SmtpConfigController smtpConfigController;

    private SmtpConfigRequestDto requestDto;
    private SmtpConfigResponseDto responseDto;
    private UUID configId;
    private UriComponentsBuilder uriBuilder;

    @BeforeEach
    void setUp() {
        configId = UUID.randomUUID();

        requestDto =
                SmtpConfigRequestDto.builder()
                        .host("smtp.example.com")
                        .port("587")
                        .username("user@example.com")
                        .password("password")
                        .fromEmail("from@example.com")
                        .useSsl(true)
                        .useTls(false)
                        .build();

        responseDto =
                SmtpConfigResponseDto.builder()
                        .id(configId)
                        .host("smtp.example.com")
                        .port(587)
                        .username("user@example.com")
                        .fromEmail("from@example.com")
                        .useSsl(true)
                        .useTls(false)
                        .build();

        uriBuilder = UriComponentsBuilder.newInstance().path("/api");
    }

    @Test
    void createSmtpConfig_ShouldReturnCreatedAndUri() {
        // Arrange
        when(smtpConfigService.createSmtpConfig(any(SmtpConfigRequestDto.class)))
                .thenReturn(responseDto);

        // Act
        ResponseEntity<SmtpConfigResponseDto> response =
                smtpConfigController.createSmtpConfig(requestDto, uriBuilder);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains(configId.toString()));
        verify(smtpConfigService).createSmtpConfig(eq(requestDto));
    }

    @Test
    void getAllSmtpConfigs_ShouldReturnAllConfigs() {
        // Arrange
        List<SmtpConfigResponseDto> configs = List.of(responseDto);
        when(smtpConfigService.getAllSmtpConfigs()).thenReturn(configs);

        // Act
        ResponseEntity<List<SmtpConfigResponseDto>> response =
                smtpConfigController.getAllSmtpConfigs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(smtpConfigService).getAllSmtpConfigs();
    }

    @Test
    void getSmtpConfigById_ShouldReturnConfig() {
        // Arrange
        when(smtpConfigService.getSmtpConfigById(configId.toString())).thenReturn(responseDto);

        // Act
        ResponseEntity<SmtpConfigResponseDto> response =
                smtpConfigController.getSmtpConfigById(configId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(configId, response.getBody().getId());
        verify(smtpConfigService).getSmtpConfigById(configId.toString());
    }

    @Test
    void updateSmtpConfig_ShouldReturnUpdatedConfig() {
        // Arrange
        when(smtpConfigService.updateSmtpConfig(
                        eq(configId.toString()), any(SmtpConfigRequestDto.class)))
                .thenReturn(responseDto);

        // Act
        ResponseEntity<SmtpConfigResponseDto> response =
                smtpConfigController.updateSmtpConfig(configId.toString(), requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(configId, response.getBody().getId());
        verify(smtpConfigService).updateSmtpConfig(configId.toString(), requestDto);
    }

    @Test
    void deleteSmtpConfig_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(smtpConfigService).deleteSmtpConfig(configId.toString());

        // Act
        ResponseEntity<Void> response = smtpConfigController.deleteSmtpConfig(configId.toString());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(smtpConfigService).deleteSmtpConfig(configId.toString());
    }
}
