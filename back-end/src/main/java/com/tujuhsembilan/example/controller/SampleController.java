package com.tujuhsembilan.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.example.exception.MultipleException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/sample")
public class SampleController {

  private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

  @PostMapping("/log-error")
  public void logError(@RequestBody LogMessage logMessage){
    logger.error("FE error: {}", logMessage.getMessage());
    logger.error("Stack Trace: {}", logMessage.getStackTrace());
  }

  @Getter
  @Setter
  public static class LogMessage {
    private String message;
    private String stackTrace;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class SampleRequestBody {
    @Size(max = 10)
    private String input;

    @NotBlank
    private String mustHaveSomething;
  }

  @PostMapping("/post")
    public ResponseEntity<?> postSomething(@Valid @RequestBody SampleRequestBody body) {
        try {
            // Your business logic here
            throw new EntityNotFoundException("Test");
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage(), e);
            throw e; // re-throw the exception to be handled globally or by Spring
        }
    }

    @GetMapping("/multiple-exception")
    public ResponseEntity<?> multipleException() {
        try {
            // Your business logic here
            throw new MultipleException(new EntityNotFoundException("Test1"), new IndexOutOfBoundsException("Test2"));
        } catch (MultipleException e) {
            logger.error("Multiple exceptions occurred: {}", e.getMessage(), e);
            throw e; // re-throw the exception to be handled globally or by Spring
        }
    }

}
