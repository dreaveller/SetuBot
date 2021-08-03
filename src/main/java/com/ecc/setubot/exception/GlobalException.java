package com.ecc.setubot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @Description: 全局异常处理
 * @author: ecc
 * @date: 2020/1/23 14:03
 **/
@ControllerAdvice
public class GlobalException extends Exception {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(Exception.class)
    public void defaultErrorHandler(Exception e) {
        try {
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            logger.error("全局异常处理：", e);
        } catch (Exception ex) {
            logger.error("全局异常处理异常：", ex);
        }
    }

    @ExceptionHandler(APIException.class)
    public void MyExceptionHandler(APIException e) {
        try {
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
            System.out.println("myexception");
        }catch (Exception ex) {

        }
    }

}
