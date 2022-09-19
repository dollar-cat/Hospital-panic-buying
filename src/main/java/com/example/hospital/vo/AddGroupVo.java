package com.example.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGroupVo {
    private String remark;
    private String subjectId;
    private String subjectsId;
    private String docterId;
    private String registerDate;
    private Integer registerTime;
    private Long startTime;
    private String cardId;
}
