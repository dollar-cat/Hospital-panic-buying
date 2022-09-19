package com.example.hospital.pojo;

import com.example.hospital.dto.GetOrderDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupUser{
    private String snowflakeId;
    private String cardId;
    private String cookie;
    private Integer usage;
    private Boolean isActive;
    private Boolean isSnatch;
    private String createDate;
    private String remark;
    private String userName;
    private Boolean isOrder;
    private String hospitalUserId;
}
