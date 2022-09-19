package com.example.hospital.controller;

import com.example.hospital.vo.AddUserVo;
import com.example.hospital.data.Cache;
import com.example.hospital.resp.RespBean;
import com.example.hospital.resp.RespBeanEnum;
import com.example.hospital.service.DocterService;
import com.example.hospital.service.UserService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    DocterService docterService;

    @RequestMapping("add")
    public RespBean add(@RequestBody AddUserVo user) throws IOException {
       String result = userService.add(user);

       if(result.equals("success")){
           docterService.initData();
           return RespBean.success(RespBeanEnum.ADD_USER);
       }

       return RespBean.error(RespBeanEnum.ERROR,result);
    }

    @GetMapping("toList")
    public RespBean userList(){
        return RespBean.success(Cache.getUserList());
    }

    @GetMapping("list")
    public RespBean list(){
        return RespBean.success(userService.selectList());
    }

    @GetMapping("stop/{cardId}")
    public RespBean stopUser(@PathVariable String cardId){
        Integer result = userService.stop(cardId);
        return result < 1 ? RespBean.error(RespBeanEnum.ERROR) : RespBean.success();
    }

    @GetMapping("remove/{cardId}")
    public RespBean removeUser(@PathVariable String cardId){
        Integer result = userService.remove(cardId);
        return result < 1 ? RespBean.error(RespBeanEnum.ERROR) : RespBean.success();
    }

}
