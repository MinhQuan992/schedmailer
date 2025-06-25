package com.schedmailer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.schedmailer.dto.smtpconfig.SmtpConfigRequestDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;
import com.schedmailer.domain.entity.SmtpConfig;
import com.schedmailer.exception.ResourceNotFoundException;
import com.schedmailer.mapper.SmtpConfigMapper;
import com.schedmailer.repository.SmtpConfigRepository;
import com.schedmailer.util.EncryptionUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SmtpConfigServiceTest {

    @Mock private SmtpConfigRepository smtpConfigRepository;

    @Mock private SmtpConfigMapper smtpConfigMapper;

    @InjectMocks private SmtpConfigService smtpConfigService;

    private final String encryptionKey = "testEncryptionKey";
    private final String testId = UUID.randomUUID().toString();
    private SmtpConfigRequestDto requestDto;
    private SmtpConfig smtpConfig;
    private SmtpConfigResponseDto responseDto;

    private static final String TEST_HOST = "smtp.example.com";
    private static final String TEST_PORT_STRING = "587";
    private static final int TEST_PORT_INT = 587;
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_ENCRYPTED_PASSWORD = "encryptedPassword";
    private static final String TEST_FROM_EMAIL = "test@example.com";
    private static final boolean TEST_USE_SSL = true;
    private static final boolean TEST_USE_TLS = false;

    @BeforeEach
    void setUp() {
        // Set an encryption key using reflection
        ReflectionTestUtils.setField(smtpConfigService, "encryptionKey", encryptionKey);

        // Setup test data using builder pattern
        requestDto =
                SmtpConfigRequestDto.builder()
                        .host(TEST_HOST)
                        .port(TEST_PORT_STRING)
                        .username(TEST_USERNAME)
                        .password(TEST_PASSWORD)
                        .fromEmail(TEST_FROM_EMAIL)
                        .useSsl(TEST_USE_SSL)
                        .useTls(TEST_USE_TLS)
                        .build();

        smtpConfig =
                SmtpConfig.builder()
                        .id(UUID.fromString(testId))
                        .host(TEST_HOST)
                        .port(TEST_PORT_INT)
                        .username(TEST_USERNAME)
                        .password(TEST_ENCRYPTED_PASSWORD)
                        .fromEmail(TEST_FROM_EMAIL)
                        .useSsl(TEST_USE_SSL)
                        .useTls(TEST_USE_TLS)
                        .build();

        responseDto =
                SmtpConfigResponseDto.builder()
                        .id(UUID.fromString(testId))
                        .host(TEST_HOST)
                        .port(TEST_PORT_INT)
                        .username(TEST_USERNAME)
                        .fromEmail(TEST_FROM_EMAIL)
                        .useSsl(TEST_USE_SSL)
                        .useTls(TEST_USE_TLS)
                        .build();
    }

    @Test
    void createSmtpConfig_ShouldCreateAndReturnSmtpConfig() {
        // Arrange
        try (MockedStatic<EncryptionUtil> encryptionUtilMockedStatic =
                mockStatic(EncryptionUtil.class)) {
            encryptionUtilMockedStatic
                    .when(() -> EncryptionUtil.encrypt(anyString(), anyString()))
                    .thenReturn(TEST_ENCRYPTED_PASSWORD);

            when(smtpConfigRepository.save(any(SmtpConfig.class))).thenReturn(smtpConfig);
            when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class)))
                    .thenReturn(responseDto);

            // Act
            SmtpConfigResponseDto result = smtpConfigService.createSmtpConfig(requestDto);

            // Assert
            assertNotNull(result);
            assertEquals(responseDto, result);
            verify(smtpConfigRepository).save(any(SmtpConfig.class));
            verify(smtpConfigMapper).smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class));
            encryptionUtilMockedStatic.verify(
                    () -> EncryptionUtil.encrypt(eq(TEST_PASSWORD), eq(encryptionKey)));
        }
    }

    @Test
    void getAllSmtpConfigs_ShouldReturnAllConfigs() {
        // Arrange
        SmtpConfig smtpConfig2 =
                SmtpConfig.builder()
                        .id(UUID.randomUUID())
                        .host("smtp2.example.com")
                        .port(465)
                        .username("username2")
                        .password("encryptedPassword2")
                        .fromEmail("test2@example.com")
                        .useSsl(false)
                        .useTls(true)
                        .build();

        SmtpConfigResponseDto responseDto2 =
                SmtpConfigResponseDto.builder()
                        .id(smtpConfig2.getId())
                        .host("smtp2.example.com")
                        .port(465)
                        .username("username2")
                        .fromEmail("test2@example.com")
                        .useSsl(false)
                        .useTls(true)
                        .build();

        List<SmtpConfig> smtpConfigs = Arrays.asList(smtpConfig, smtpConfig2);

        when(smtpConfigRepository.findAll()).thenReturn(smtpConfigs);
        when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(smtpConfig))
                .thenReturn(responseDto);
        when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(smtpConfig2))
                .thenReturn(responseDto2);

        // Act
        List<SmtpConfigResponseDto> results = smtpConfigService.getAllSmtpConfigs();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(responseDto, results.get(0));
        assertEquals(responseDto2, results.get(1));
        verify(smtpConfigRepository).findAll();
        verify(smtpConfigMapper, times(2)).smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class));
    }

    @Test
    void getSmtpConfigById_ShouldReturnConfig_WhenIdExists() {
        // Arrange
        when(smtpConfigRepository.findById(UUID.fromString(testId)))
                .thenReturn(Optional.of(smtpConfig));
        when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(smtpConfig))
                .thenReturn(responseDto);

        // Act
        SmtpConfigResponseDto result = smtpConfigService.getSmtpConfigById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(smtpConfigRepository).findById(UUID.fromString(testId));
        verify(smtpConfigMapper).smtpConfigToSmtpConfigResponseDto(smtpConfig);
    }

    @Test
    void getSmtpConfigById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(smtpConfigRepository.findById(UUID.fromString(testId))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> smtpConfigService.getSmtpConfigById(testId));

        assertTrue(exception.getMessage().contains(testId));
        verify(smtpConfigRepository).findById(UUID.fromString(testId));
        verify(smtpConfigMapper, never()).smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class));
    }

    @Test
    void updateSmtpConfig_ShouldUpdateAndReturnConfig_WhenIdExists() {
        // Arrange
        try (MockedStatic<EncryptionUtil> encryptionUtilMockedStatic =
                mockStatic(EncryptionUtil.class)) {
            encryptionUtilMockedStatic
                    .when(() -> EncryptionUtil.encrypt(anyString(), anyString()))
                    .thenReturn("newEncryptedPassword");

            when(smtpConfigRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(smtpConfig));
            when(smtpConfigRepository.save(any(SmtpConfig.class))).thenReturn(smtpConfig);
            when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class)))
                    .thenReturn(responseDto);

            // Act
            SmtpConfigResponseDto result = smtpConfigService.updateSmtpConfig(testId, requestDto);

            // Assert
            assertNotNull(result);
            assertEquals(responseDto, result);
            verify(smtpConfigRepository).findById(UUID.fromString(testId));
            verify(smtpConfigRepository).save(smtpConfig);
            verify(smtpConfigMapper).smtpConfigToSmtpConfigResponseDto(smtpConfig);
            encryptionUtilMockedStatic.verify(
                    () -> EncryptionUtil.encrypt(eq(TEST_PASSWORD), eq(encryptionKey)));
        }
    }

    @Test
    void updateSmtpConfig_ShouldNotEncryptPassword_WhenPasswordIsEmpty() {
        // Arrange
        SmtpConfigRequestDto emptyPasswordDto =
                SmtpConfigRequestDto.builder()
                        .host(TEST_HOST)
                        .port(TEST_PORT_STRING)
                        .username(TEST_USERNAME)
                        .password("")
                        .fromEmail(TEST_FROM_EMAIL)
                        .useSsl(TEST_USE_SSL)
                        .useTls(TEST_USE_TLS)
                        .build();

        when(smtpConfigRepository.findById(any(UUID.class))).thenReturn(Optional.of(smtpConfig));
        when(smtpConfigRepository.save(any(SmtpConfig.class))).thenReturn(smtpConfig);
        when(smtpConfigMapper.smtpConfigToSmtpConfigResponseDto(any(SmtpConfig.class)))
                .thenReturn(responseDto);

        // Act
        SmtpConfigResponseDto result = smtpConfigService.updateSmtpConfig(testId, emptyPasswordDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(smtpConfigRepository).findById(UUID.fromString(testId));
        verify(smtpConfigRepository).save(smtpConfig);
        // Password should not be changed
        assertEquals(TEST_ENCRYPTED_PASSWORD, smtpConfig.getPassword());
    }

    @Test
    void deleteSmtpConfig_ShouldDeleteConfig_WhenIdExists() {
        // Arrange
        when(smtpConfigRepository.findById(UUID.fromString(testId)))
                .thenReturn(Optional.of(smtpConfig));
        doNothing().when(smtpConfigRepository).deleteById(UUID.fromString(testId));

        // Act
        smtpConfigService.deleteSmtpConfig(testId);

        // Assert
        verify(smtpConfigRepository).findById(UUID.fromString(testId));
        verify(smtpConfigRepository).deleteById(UUID.fromString(testId));
    }

    @Test
    void deleteSmtpConfig_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(smtpConfigRepository.findById(UUID.fromString(testId))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> smtpConfigService.deleteSmtpConfig(testId));

        assertTrue(exception.getMessage().contains(testId));
        verify(smtpConfigRepository).findById(UUID.fromString(testId));
        verify(smtpConfigRepository, never()).deleteById(any(UUID.class));
    }
}
