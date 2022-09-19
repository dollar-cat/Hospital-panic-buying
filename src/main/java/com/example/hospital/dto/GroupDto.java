package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private String id;
    private String createDate;
    private String remark;
    private Integer userSize;
    private Integer taskType;
    private Boolean isActive;
    private String docter;
    private String taskStartDate;
    private String registerDate;
    private Integer orderNumber;
    private Integer activeUserSize;
}
