package org.app.instagram_be.exception.handler;

import org.app.instagram_be.exception.UserNameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<?> handleUserNameNotFoundException(UserNameNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
