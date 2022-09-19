package com.example.hospital.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private Integer code;
    private String msg;
    private Object data;

    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMsg(),null);
    }

    public static RespBean success(Object data){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMsg(),data);
    }

    public static RespBean success(RespBeanEnum r){
        return new RespBean(r.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMsg(),null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMsg(),null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum,Object error){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMsg(),error);
    }

}
