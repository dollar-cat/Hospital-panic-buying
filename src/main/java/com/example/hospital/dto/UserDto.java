package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;
    private String remark;
    private String phone;
    private String cardType;
    private Integer setNumber;
    private String identityName;
    private String createName;
    private String createDate;
}
