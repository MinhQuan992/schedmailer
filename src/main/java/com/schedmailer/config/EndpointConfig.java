package com.schedmailer.config;

public class EndpointConfig {
    private EndpointConfig() {}

    public static final String ENDPOINT_V1 = "/api/v1";

    public static final String BY_ID = "/{id}";

    public static final String SMTP_CONFIG = "/smtp-config";
    public static final String SMTP_CONFIG_BY_ID = SMTP_CONFIG + BY_ID;

    public static final String EMAIL_SCHEDULING = "/email-scheduling";
    public static final String EMAIL_SCHEDULING_SCHEDULE = "/schedule";
}
