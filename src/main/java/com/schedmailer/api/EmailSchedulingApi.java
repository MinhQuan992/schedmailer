package com.schedmailer.api;

import static com.schedmailer.config.EndpointConfig.EMAIL_SCHEDULING;
import static com.schedmailer.config.EndpointConfig.EMAIL_SCHEDULING_SCHEDULE;
import static com.schedmailer.config.EndpointConfig.ENDPOINT_V1;

import com.schedmailer.dto.scheduledemail.ScheduledEmailRequestDto;
import com.schedmailer.dto.scheduledemail.ScheduledEmailResponseDto;

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

@RequestMapping(ENDPOINT_V1 + EMAIL_SCHEDULING)
@Tag(
        name = "Email Scheduling",
        description = "API for scheduling emails to be sent at specific times or immediately")
public interface EmailSchedulingApi {
    @Operation(
            summary = "Schedule a new email",
            description =
                    "Schedules an email to be sent at a specified future time or immediately if no schedule time is provided, using the provided SMTP configuration")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Email scheduled successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                ScheduledEmailResponseDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid email scheduling data provided",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "SMTP configuration not found",
                        content = @Content)
            })
    @PostMapping(EMAIL_SCHEDULING_SCHEDULE)
    ResponseEntity<ScheduledEmailResponseDto> scheduleEmail(
            @RequestBody
                    @Valid
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Email scheduling details",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    ScheduledEmailRequestDto
                                                                            .class)))
                    ScheduledEmailRequestDto request);
}
