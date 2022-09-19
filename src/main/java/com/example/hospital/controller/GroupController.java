package com.example.hospital.controller;

import com.example.hospital.dto.GroupInsideUserDto;
import com.example.hospital.pojo.Group;
import com.example.hospital.vo.AddGroupUserVo;
import com.example.hospital.vo.AddGroupVo;
import com.example.hospital.data.Cache;
import com.example.hospital.resp.RespBean;
import com.example.hospital.resp.RespBeanEnum;
import com.example.hospital.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @GetMapping("/list")
    public RespBean list(){
        return RespBean.success(Cache.getGroupListDtoList());
    }

    @PostMapping("/create")
    public RespBean create(@RequestBody AddGroupVo addGroupVo){
        int result = groupService.create(addGroupVo);
        return result < 1 ? RespBean.error(RespBeanEnum.CREATE_ERROR) : RespBean.success();
    }

    @PostMapping("/addUser")
    public RespBean addUser(@RequestBody AddGroupUserVo addGroupUserVo){
        int result = groupService.addUser(addGroupUserVo);
        return result < 1 ? RespBean.error(RespBeanEnum.GROUP_ADD_USER_ERROR) : RespBean.success();
    }

    @GetMapping("/inside/user/{groupId}")
    public RespBean toUserList(@PathVariable(value = "groupId") String groupId){
       List<GroupInsideUserDto> groupInsideUserDto = groupService.insideUser(groupId);
       return RespBean.success(groupInsideUserDto);
    }

    @GetMapping("/delect/{groupId}")
    public RespBean delectGroup(@PathVariable String groupId){
       int result = groupService.delectGroupById(groupId);
       return result < 1 ? RespBean.error(RespBeanEnum.DELECT_ERROR) : RespBean.success();
    }

    @GetMapping("/stop/user/{groupId}/{snowflakeId}")
    public RespBean stopUser(@PathVariable String groupId,@PathVariable(value = "snowflakeId") String userId){
        int result = groupService.stopGroupUserById(groupId,userId);
        return result < 1 ? RespBean.error(RespBeanEnum.DELECT_ERROR) : RespBean.success();
    }

    @GetMapping("/delect/{groupId}/{snowflakeId}")
    public RespBean delectUser(@PathVariable String groupId,@PathVariable(value = "snowflakeId") String userId){
        int result = groupService.delectGroupUserById(groupId,userId);
        return result < 1 ? RespBean.error(RespBeanEnum.DELECT_ERROR) : RespBean.success();
    }
}
