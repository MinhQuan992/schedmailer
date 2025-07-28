package com.schedmailer.service;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.domain.entity.SmtpConfig;
import com.schedmailer.domain.enums.EmailStatus;
import com.schedmailer.dto.scheduledemail.ScheduledEmailRequestDto;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;
import com.schedmailer.exception.ResourceNotFoundException;
import com.schedmailer.mapper.ScheduledEmailMapper;
import com.schedmailer.repository.ScheduledEmailRepository;
import com.schedmailer.repository.SmtpConfigRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSchedulingService {
    private final ScheduledEmailRepository scheduledEmailRepository;
    private final SmtpConfigRepository smtpConfigRepository;
    private final ScheduledEmailMapper scheduledEmailMapper;

    @Transactional
    public ScheduledEmailResponseDto scheduleEmail(ScheduledEmailRequestDto request) {
        SmtpConfig smtpConfig =
                smtpConfigRepository
                        .findById(request.smtpConfigId())
                        .orElseThrow(() -> new ResourceNotFoundException("SMTP Config not found"));

        ScheduledEmail email =
                ScheduledEmail.builder()
                        .toEmail(request.toEmail())
                        .subject(request.subject())
                        .body(request.body())
                        .shouldSendAt(request.shouldSendAt())
                        .status(EmailStatus.PENDING)
                        .retryCount(0)
                        .smtpConfig(smtpConfig)
                        .build();
        ScheduledEmail scheduledEmail = scheduledEmailRepository.save(email);

        return scheduledEmailMapper.scheduledEmailToScheduledEmailResponseDto(scheduledEmail);
    }
}
