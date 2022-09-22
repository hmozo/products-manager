package com.techtalk.productsservice.interfaces.rest.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.distributed.CommandDispatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CreateProductControllerExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> onBadRequestException(IllegalArgumentException exception){
        log.info("IllegalArgumentException: " + exception);
        return handleExceptionInternal(exception,exception.getMessage(),null, HttpStatus.BAD_REQUEST,null);
    }

    @ExceptionHandler({CommandExecutionException.class, CommandDispatchException.class})
    public ResponseEntity<Object> onSendCommandException(CommandExecutionException exception){
        log.info("CommandExecutionException/CommandDispatchException: " + exception);
        return handleExceptionInternal(exception,exception.getMessage(),null, HttpStatus.BAD_REQUEST,null);
    }

    /*
    @ExceptionHandler({AxonServerRemoteCommandHandlingException.class})
    public ResponseEntity<Object> onSendCommandException(AxonServerRemoteCommandHandlingException exception){
        log.info("Exception sending Command: " + exception);
        return handleExceptionInternal(exception,exception.getMessage(),null, HttpStatus.BAD_REQUEST,null);
    }

     */

    public static List<String> getListErrors(BindingResult bindingResult){
        List<String> result= new ArrayList<>();
        bindingResult.getAllErrors().stream()
                .forEach((e)->result.add(e.getDefaultMessage()));

        return result;
    }


}
