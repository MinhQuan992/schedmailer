package com.schedmailer.service.job;

import com.schedmailer.domain.entity.ScheduledEmail;
import com.schedmailer.domain.enums.EmailStatus;
import com.schedmailer.repository.ScheduledEmailRepository;
import com.schedmailer.service.EmailSenderService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailRetryScheduler {
    private final ScheduledEmailRepository scheduledEmailRepository;
    private final EmailSenderService emailSenderService;

    private static final int MAX_RETRIES = 5;
    private static final long BASE_RETRY_DELAY_SECONDS = 60;

    @Scheduled(fixedDelay = 10000) // Every 10 seconds
    @Transactional
    public void processScheduledEmails() {
        List<ScheduledEmail> emails =
                scheduledEmailRepository.findPendingOrRetryingDue(ZonedDateTime.now());
        log.info("Found {} email(s) to process", emails.size());

        for (ScheduledEmail email : emails) {
            try {
                log.info("Attempting to send email: {}", email.getId());
                emailSenderService.send(email);
                email.setStatus(EmailStatus.SENT);
                email.setSentAt(ZonedDateTime.now());
                log.info("Email {} sent successfully", email.getId());
            } catch (Exception ex) {
                log.error("Email {} failed: {}", email.getId(), ex.getMessage());
                int attempts = email.getRetryCount() + 1;
                if (attempts > MAX_RETRIES) {
                    email.setStatus(EmailStatus.FAILED);
                    log.warn(
                            "Email {} marked as FAILED after {} retries",
                            email.getId(),
                            MAX_RETRIES);
                } else {
                    email.setRetryCount(attempts);
                    email.setStatus(EmailStatus.RETRYING);
                    long backoffSeconds = (long) (Math.pow(2, attempts) * BASE_RETRY_DELAY_SECONDS);
                    email.setShouldSendAt(ZonedDateTime.now().plusSeconds(backoffSeconds));
                    log.info(
                            "Email {} scheduled for retry in {} seconds",
                            email.getId(),
                            backoffSeconds);
                }
            } finally {
                scheduledEmailRepository.save(email);
            }
        }
    }
}
