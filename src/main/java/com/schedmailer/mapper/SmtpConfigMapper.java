package com.schedmailer.mapper;

import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;
import com.schedmailer.entity.SmtpConfig;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmtpConfigMapper {
    SmtpConfigResponseDto smtpConfigToSmtpConfigResponseDto(SmtpConfig smtpConfig);
}
