package com.schedmailer.domain.entity;

import com.schedmailer.domain.enums.EmailStatus;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scheduled_emails")
public class ScheduledEmail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    private ZonedDateTime shouldSendAt;

    private ZonedDateTime sentAt;

    @Column(nullable = false)
    private int retryCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmtpConfig smtpConfig;
}
