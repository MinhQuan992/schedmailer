# Schedmailer

**Schedmailer** is a backend-only, self-hostable email scheduling and retry service built with Spring Boot. It allows developers to send scheduled emails via custom SMTP configurations, supports retries with exponential backoff, and provides RESTful APIs for seamless integration with other services.

---

## ✨ Features

- 📧 Schedule emails to be sent at specific times.
- 🔁 Retry failed emails automatically with exponential backoff.
- 🔒 Secure SMTP credential encryption using environment-based key.
- 📦 RESTful API for managing email jobs and SMTP configurations.
- 📄 Docker + Docker Compose support for easy deployment.
- 🕵️ Detailed status tracking of email delivery attempts.

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3
- PostgreSQL
- JavaMail (SMTP support)
- Docker & Docker Compose

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/MinhQuan992/schedmailer.git
cd schedmailer
```

### 2. 🛠 Environment Configuration

Before running Schedmailer, create a `.env` file in the root directory with the following variables:

```dotenv
# Database Credentials
DATA_SOURCE_USERNAME=postgres
DATA_SOURCE_PASSWORD=your_password

# 32-character key used for encrypting SMTP passwords
ENCRYPTION_KEY=your-32-character-secret-key
```

### 3. 🚀 Build and Run with Docker Compose
To get started, ensure Docker and Docker Compose are installed, then use the included `docker-compose.yaml` file to launch both the application and PostgreSQL.
```bash
docker compose up
```
This will:
- Spin up the schedmailer-api Spring Boot app.
- Spin up a postgres container with persistent storage.
- Load environment variables from `.env`.

Once running, the API is accessible at: http://localhost:8080.

You can view the full API documentation at: http://localhost:8080/api-docs/swagger-ui/index.html.

---

## 📬 SMTP Configuration

Schedmailer supports multiple SMTP configurations. Each email job is linked to a specific SMTP config (host, port, username, password).

The password is AES-encrypted in the database using your `ENCRYPTION_KEY`.

---

## 🧑‍💻 Contributing

Feel free to fork the repo, open issues, or submit PRs. Schedmailer aims to become a useful backend service for developers who need robust email job scheduling in their applications.

---

## 📄 License

MIT License

---

## 🙋‍♂️ Author

Created by Quan Vo Tran Minh
