package com.schedmailer.config;

public class EndpointConfig {
    private EndpointConfig() {}

    public static final String ENDPOINT_V1 = "/api/v1";

    public static final String BY_ID = "/{id}";

    public static final String SMTP_CONFIG = "/smtpconfig";
    public static final String SMTP_CONFIG_BY_ID = SMTP_CONFIG + BY_ID;
}
