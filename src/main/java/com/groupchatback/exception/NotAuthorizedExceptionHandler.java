package com.groupchatback.exception;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.groupchatback.controller.ImageController;
import com.groupchatback.controller.UserController;
import com.groupchatback.controller.admin.LoginController_Admin;
import com.groupchatback.controller.admin.RegisterController_Admin;
import com.groupchatback.controller.admin.UserController_Admin;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Order(2)
@ControllerAdvice(
        annotations = RestController.class,
        basePackageClasses = {
                UserController.class,
                ImageController.class,
                UserController_Admin.class,
                RegisterController_Admin.class,
                LoginController_Admin.class
        })
public class NotAuthorizedExceptionHandler {

    @ExceptionHandler(value = {NotAuthorizedException.class})
    public ResponseEntity<?> handleRequest(RuntimeException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(e.toString(), status);
    }

}
