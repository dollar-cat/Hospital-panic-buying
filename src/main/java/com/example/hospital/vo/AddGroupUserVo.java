package com.example.hospital.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGroupUserVo {
    @JsonProperty(value = "selectGroupId")
    private String groupId;
    @JsonProperty(value = "selectActiveUser")
    private List<IdObjVo> userId;

}
