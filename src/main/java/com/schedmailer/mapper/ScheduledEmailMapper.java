package com.schedmailer.mapper;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduledEmailMapper {
    @Mapping(source = "smtpConfig.id", target = "smtpConfigId")
    ScheduledEmailResponseDto scheduledEmailToScheduledEmailResponseDto(
            ScheduledEmail scheduledEmail);
}
