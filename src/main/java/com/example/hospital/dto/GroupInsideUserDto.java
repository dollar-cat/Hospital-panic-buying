package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInsideUserDto {
    private String groupId;
    private String snowflakeId;
    private String userId;
    private String cookie;
    private Integer usage;
    private Boolean isActive;
    private Boolean isSnatch;
    private String remark;
    private String createDate;
    private String userName;
    private Boolean isOrder;
}
