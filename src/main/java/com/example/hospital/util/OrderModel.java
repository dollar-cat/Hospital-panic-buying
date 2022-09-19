package com.example.hospital.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private String hospitalUserId;
    private String resourceId;
    private String registDate;
    private String url;
    private String docCode;
    private String docDuty;
    private String deptCode;
    private String deptName;
    private String docName;
}
