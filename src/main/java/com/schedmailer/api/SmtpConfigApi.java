package com.schedmailer.api;

import com.schedmailer.dto.smtpconfig.SmtpConfigCreateDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import static com.schedmailer.common.constants.EndpointConfig.ENDPOINT_V1;
import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG;

@RequestMapping(ENDPOINT_V1 + SMTP_CONFIG)
@Tag(name = "SMTP Configuration", description = "API for managing SMTP configurations")
public interface SmtpConfigApi {
    @Operation(
            summary = "Create a new SMTP configuration",
            description = "Creates a new SMTP server configuration for sending emails")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "SMTP configuration created successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SmtpConfigDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid SMTP configuration data provided",
                        content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "SMTP configuration already exists",
                        content = @Content)
            })
    @PostMapping
    ResponseEntity<SmtpConfigDto> createSmtpConfig(
            @RequestBody
                    @Valid
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "SMTP configuration details to create",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    SmtpConfigCreateDto.class)))
                    SmtpConfigCreateDto smtpConfigCreateDto,
            UriComponentsBuilder ucb);
}
