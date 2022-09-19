package com.example.hospital.service;

import com.example.hospital.dto.GroupInsideUserDto;
import com.example.hospital.vo.AddGroupUserVo;
import com.example.hospital.vo.AddGroupVo;

import java.util.List;

public interface GroupService {
    int create(AddGroupVo addGroupVo);

    int addUser(AddGroupUserVo addGroupUserVo);

    List<GroupInsideUserDto> insideUser(String groupId);

    int delectGroupById(String groupId);

    int stopGroupUserById(String groupId,String userId);

    int delectGroupUserById(String groupId, String userId);

    void delectGroupUserAllByCardId(String cardId);
}
