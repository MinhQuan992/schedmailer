package com.schedmailer.repository;

import com.schedmailer.domain.entity.SmtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SmtpConfigRepository extends JpaRepository<SmtpConfig, UUID> {}
