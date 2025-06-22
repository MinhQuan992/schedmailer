package com.schedmailer.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private WebRequest webRequest;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void handleMethodArgumentNotValid_ShouldReturnValidationErrors() {
        // Arrange
        FieldError error1 = new FieldError("object", "field1", "Error message 1");
        FieldError error2 = new FieldError("object", "field2", "Error message 2");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(error1, error2));

        // Act
        ResponseEntity<Object> response = controllerExceptionHandler.handleMethodArgumentNotValid(
                exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        assertNotNull(body);
        
        List<String> errors = body.get("errors");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.contains("Error message 1"));
        assertTrue(errors.contains("Error message 2"));
    }
}
