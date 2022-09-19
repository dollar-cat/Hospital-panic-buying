package com.example.hospital.service;

import com.example.hospital.vo.AddUserVo;
import com.example.hospital.dto.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public String add(AddUserVo user) throws IOException;

    Integer remove(String cardId);

    Integer stop(String cardId);

    List<UserDto> selectList();
}
