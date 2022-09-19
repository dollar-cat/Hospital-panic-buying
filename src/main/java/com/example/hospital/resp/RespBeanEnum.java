package com.example.hospital.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum RespBeanEnum {
    SUCCESS(200,"处理成功"),
    ADD_USER(210,"添加用户成功"),
    ERROR(300,"处理失败"),
    UNKNOWN_ERROR(301,"未处理的异常！"),
    COOKIE_ERROR(311,"错误或过期的COOKIE"),
    COOKIE_FORM_ERROR(312,"错误的COOKIE格式"),
    APIREQU_ERROR(320,"失败的请求"),
    TIMEOUT_ERROR(321,"请求源请求超时"),
    GET_DOCTER_ERROR(601,"获取医生数据失败"),
    CREATE_ERROR(700,"创建组失败"),
    GROUP_ADD_USER_ERROR(701,"添加用户失败!"),
    DELECT_ERROR(702,"删除组失败!"),
    DELECT_USER_ERROR(703,"删除组用户失败!"),
    GROUP_STOP_USER_ERROR(704,"操作失败了!");

    private final Integer code;
    private final String msg;
}
