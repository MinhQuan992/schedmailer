package com.schedmailer.api;

import static com.schedmailer.common.constants.EndpointConfig.BY_ID;
import static com.schedmailer.common.constants.EndpointConfig.ENDPOINT_V1;
import static com.schedmailer.common.constants.EndpointConfig.SMTP_CONFIG;

import com.schedmailer.dto.smtpconfig.SmtpConfigRequestDto;
import com.schedmailer.dto.smtpconfig.SmtpConfigResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                SmtpConfigResponseDto.class))),
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
    ResponseEntity<SmtpConfigResponseDto> createSmtpConfig(
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
                                                                    SmtpConfigRequestDto.class)))
                    SmtpConfigRequestDto smtpConfigRequestDto,
            UriComponentsBuilder ucb);

    @Operation(
            summary = "Get all SMTP configurations",
            description = "Retrieves all SMTP configurations")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of all SMTP configurations",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                SmtpConfigResponseDto.class)))
            })
    @GetMapping
    ResponseEntity<List<SmtpConfigResponseDto>> getAllSmtpConfigs();

    @Operation(
            summary = "Get SMTP configuration by ID",
            description = "Retrieves a specific SMTP configuration by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "SMTP configuration found",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                SmtpConfigResponseDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid UUID format provided",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "SMTP configuration not found",
                        content = @Content)
            })
    @GetMapping(BY_ID)
    ResponseEntity<SmtpConfigResponseDto> getSmtpConfigById(@PathVariable String id);

    @Operation(
            summary = "Update SMTP configuration",
            description = "Updates an existing SMTP configuration by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "SMTP configuration updated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                SmtpConfigResponseDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description =
                                "Invalid request (invalid UUID format or invalid configuration data)",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "SMTP configuration not found",
                        content = @Content)
            })
    @PutMapping(BY_ID)
    ResponseEntity<SmtpConfigResponseDto> updateSmtpConfig(
            @PathVariable String id,
            @RequestBody
                    @Valid
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Updated SMTP configuration details",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    SmtpConfigRequestDto.class)))
                    SmtpConfigRequestDto smtpConfigRequestDto);

    @Operation(
            summary = "Delete SMTP configuration",
            description = "Deletes an existing SMTP configuration by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "SMTP configuration deleted successfully",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid UUID format provided",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "SMTP configuration not found",
                        content = @Content)
            })
    @DeleteMapping(BY_ID)
    ResponseEntity<Void> deleteSmtpConfig(@PathVariable String id);
}
