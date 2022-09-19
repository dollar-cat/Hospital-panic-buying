package com.example.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.hospital.service.GroupService;
import com.example.hospital.vo.AddUserVo;
import com.example.hospital.data.Cache;
import com.example.hospital.dto.UserDto;
import com.example.hospital.dto.UserInfoDto;
import com.example.hospital.pojo.Group;
import com.example.hospital.pojo.GroupUser;
import com.example.hospital.pojo.User;
import com.example.hospital.service.UserService;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    GroupService groupService;

    @Value(value = "${userApi}")
    String userApi;
    @Value(value = "${ua}")
    String ua;

    @Override
    public String add(AddUserVo user) throws IOException {
        Headers.Builder headersBuilder = new Headers.Builder();
        headersBuilder.add("user-agent",ua);
        headersBuilder.add("cookie",user.getCookie());

        //获取用户数据
        Request request = new Request.Builder()
                .url(userApi)
                .get()
                .headers(headersBuilder.build())
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if(response.code() == 401){
            return "cookie过期或者不正确";
        }

        if(response.code() != 200){
            return "httpCode："+response.code()+"\t"+"httpMsg:"+response.message();
        }

        JSONObject result = (JSONObject) JSONObject.parse(response.body().string());
        List<UserInfoDto> userInfoDtoList = JSONObject.parseArray(result.get("bindCardList").toString(), UserInfoDto.class);

        //这个医院可绑定多个就诊人 但只返回默认的一个
        UserInfoDto resultUser = userInfoDtoList.get(0);
        User cacheUser = new User();

        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date date = new Date();
        String createDate = format.format(date);

        if(userInfoDtoList.get(0) != null){
            cacheUser.setCardId(resultUser.getCardId());
            cacheUser.setCardType(resultUser.getCardType());
            cacheUser.setCreateDate(createDate);
            cacheUser.setPhone(resultUser.getPhone());
            cacheUser.setRemark(user.getRemark());
            cacheUser.setUserName(resultUser.getUserName());
            cacheUser.setCookie(user.getCookie());
            cacheUser.setHospitalUserId(resultUser.getHospitalUserId());

            Cache.putCookie(user.getCookie(),resultUser.getCardId());
            Cache.putUser(cacheUser);
        }

        return "success";
    }

    @Override
    public Integer remove(String cardId) {
        groupService.delectGroupUserAllByCardId(cardId);
        return Cache.removeUser(cardId);
    }

    @Override
    public Integer stop(String cardId) {
        return Cache.stopUser(cardId);
    }

    @Override
    public List<UserDto> selectList() {
        List<User> userList = Cache.getUserList();
        List<UserDto> userDtoList = new ArrayList<>();

        for(User user : userList){
            UserDto userDto = new UserDto();
            userDto.setId(user.getCardId());
            userDto.setCreateName(user.getRemark());
            userDto.setIdentityName(user.getUserName());
            userDto.setPhone(user.getPhone());
            userDto.setCardType(user.getCardType());
            userDto.setRemark(user.getRemark());
            userDto.setCreateDate(user.getCreateDate());
            userDto.setSetNumber(0);
            //获取各个user存在几个组的统计
            for(Map.Entry<String, Group> entry : Cache.getGroupMapById().entrySet()){
                for(GroupUser groupUser : entry.getValue().getGroupUserList()){
                    if(user.getCardId().equals(groupUser.getCardId())){
                        userDto.setSetNumber(userDto.getSetNumber()+1);
                        break;
                    }
                }
            }
            userDtoList.add(userDto);
        }

        return userDtoList;
    }
}
