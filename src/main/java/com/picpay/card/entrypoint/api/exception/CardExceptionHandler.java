package com.picpay.card.entrypoint.api.exception;


import com.picpay.card.core.exception.ApiError;
import com.picpay.card.core.exception.BusinessException;
import com.picpay.card.core.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CardExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private final String messageFieldInvalid = "Um ou mais campos estão inválidos.";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> consumerCardNotFoundExceptionHandler(final EntityNotFoundException exception) {

        int status = HttpStatus.NOT_FOUND.value();

        ApiError apiError = ApiError.builder().
                    status(status).
                    description(exception.getMessage()).
                    build();

        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> thereIsConsumerWithThisCPFExceptionHandler(final BusinessException exception) {

        int status = HttpStatus.BAD_REQUEST.value();

        ApiError apiError = ApiError.builder().
                status(status).
                description(exception.getMessage()).
                build();

        return ResponseEntity.status(status).body(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, BindingResult bindingResult) {

        List<ObjectError> objectErrors = bindingResult.getAllErrors();

        List<String> descriptions = objectErrors.stream()
                .map(objectError ->  messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());

        int status = HttpStatus.BAD_REQUEST.value();

        ApiError apiError = ApiError.builder()
                .status(status)
                .description(messageFieldInvalid)
                .descriptions(descriptions)
                .build();

        return ResponseEntity.status(status).body(apiError);
    }

}
