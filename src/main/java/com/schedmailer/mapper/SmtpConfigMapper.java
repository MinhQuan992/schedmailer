package com.schedmailer.mapper;

import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import com.schedmailer.entity.SmtpConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SmtpConfigMapper {
    SmtpConfigMapper INSTANCE = Mappers.getMapper(SmtpConfigMapper.class);

    SmtpConfigDto smtpConfigToSmtpConfigDto(SmtpConfig smtpConfig);
}
