package com.schedmailer.controller;

import com.schedmailer.api.EmailSchedulingApi;
import com.schedmailer.dto.scheduledemail.ScheduledEmailRequestDto;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;
import com.schedmailer.service.EmailSchedulingService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailSchedulingController implements EmailSchedulingApi {
    private final EmailSchedulingService emailSchedulingService;

    @Override
    public ResponseEntity<ScheduledEmailResponseDto> scheduleEmail(
            ScheduledEmailRequestDto request) {
        ScheduledEmailResponseDto saved = emailSchedulingService.scheduleEmail(request);
        return ResponseEntity.ok(saved);
    }
}
