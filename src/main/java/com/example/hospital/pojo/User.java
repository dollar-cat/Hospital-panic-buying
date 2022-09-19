package com.example.hospital.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String cardId;
    private String createDate;
    private String remark;
    private String userName;
    private String phone;
    private String cardType;
    private Boolean isActive = true;
    private String cookie;
    private String hospitalUserId;
}
