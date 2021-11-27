package com.groupchatback.exception;

import com.groupchatback.controller.ImageController;
import com.groupchatback.controller.UserController;
import com.groupchatback.controller.admin.LoginController_Admin;
import com.groupchatback.controller.admin.RegisterController_Admin;
import com.groupchatback.controller.admin.UserController_Admin;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Order(3)
@ControllerAdvice(
        annotations = RestController.class,
        basePackageClasses = {
                UserController.class,
                ImageController.class,
                UserController_Admin.class,
                RegisterController_Admin.class,
                LoginController_Admin.class
        })
public class BadRequestExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleRequest(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        System.out.println(e);
        return new ResponseEntity<>(e.toString(),null, status);
    }

}
