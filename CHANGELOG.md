# Changelog

All notable changes to this project will be documented in this file.  
This project adheres to [Semantic Versioning](https://semver.org/).

## [1.0.0] - 2025-07-30

### Added
- Core email scheduling system with `ScheduledEmail` entity
- REST API to schedule emails with `shouldSendAt` timestamp
- Background job to poll and send emails at the scheduled time
- Retry mechanism with exponential backoff for failed deliveries
- SMTP configuration management with encrypted credentials
- Email status tracking: `PENDING`, `SENT`, `FAILED`, `RETRYING`
- Dockerfile and `docker-compose.yaml` for local and self-hosted deployments

### Changed
- N/A

### Deprecated
- N/A

### Removed
- N/A

### Fixed
- N/A

### Security
- AES-256 encryption used to secure SMTP passwords
- Encryption key injected via environment variable

### Notes
- This is a backend-only release. No UI is included.
- Designed for self-hosting or private deployment.
- SMTP credentials must be supplied by the user.
