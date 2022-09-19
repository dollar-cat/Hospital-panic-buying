package com.example.hospital.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    @JSONField(name = "cardID")
    private String cardId;
    @JSONField(name = "patientName_Sort")
    private String userName;
    @JSONField(name = "hospitalUserID")
    private String hospitalUserId;
    @JSONField(name = "tel_short4")
    private String phone;
    @JSONField(name = "cardTypeName")
    private String cardType;

}
