package com.example.hospital.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private String id;
    private String remark;
    private String registerDate;
    private Integer registerTime; //0 上午 1 下午 2前夜
    private String startTimeStr;
    private Long startTime;
    private String subjectId;
    private String subjectsId;
    private String subjectsName;
    private String docterId;
    private String docterName;
    private String docterPosition;
    private List<GroupUser> groupUserList;
    private Boolean isActive; //开启状态
    private String CreateDate;
    private Integer taskType; // 1单人轮询
    private Boolean isDelect;
    private Integer orderNumBer;
}
