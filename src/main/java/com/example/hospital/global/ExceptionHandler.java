package com.example.hospital.global;

import com.example.hospital.resp.RespBean;
import com.example.hospital.resp.RespBeanEnum;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public RespBean globalException(Exception e){
        e.printStackTrace();
        if(e instanceof IllegalArgumentException){
            return RespBean.error(RespBeanEnum.COOKIE_FORM_ERROR);
        }else if(e instanceof SocketTimeoutException){
            return RespBean.error(RespBeanEnum.TIMEOUT_ERROR);
        }
        e.printStackTrace();
        return RespBean.error(RespBeanEnum.UNKNOWN_ERROR);
    }

}
