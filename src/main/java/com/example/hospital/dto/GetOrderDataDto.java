package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDataDto {
    private String resourceID;
    private String deptName;
    private String day;
    private String timeEnd;
    private String registLevel1;
    private String resourceMemo;
    private String isAvailable;
}
