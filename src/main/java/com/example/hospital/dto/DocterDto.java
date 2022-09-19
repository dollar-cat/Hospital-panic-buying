package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//医生
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocterDto {
    private String deptCode;
    private String deptName;
    private String docCode;
    private String docName;
    private String docDuty;
    private String docDesc;
}
