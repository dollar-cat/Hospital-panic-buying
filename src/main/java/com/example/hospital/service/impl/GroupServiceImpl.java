package com.example.hospital.service.impl;

import com.example.hospital.dto.GroupInsideUserDto;
import com.example.hospital.util.OrderThread;
import com.example.hospital.util.SnowflakeIdUtil;
import com.example.hospital.vo.AddGroupUserVo;
import com.example.hospital.vo.AddGroupVo;
import com.example.hospital.data.Cache;
import com.example.hospital.pojo.Group;
import com.example.hospital.pojo.GroupUser;
import com.example.hospital.pojo.User;
import com.example.hospital.service.GroupService;
import com.example.hospital.vo.IdObjVo;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    OkHttpClient okHttpClient;

    @Override
    public int create(AddGroupVo addGroupVo) {

        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String createDate = format.format(date);
        String startTime = format.format(addGroupVo.getStartTime());

        Group group = new Group();

        String sonwflakeId = String.valueOf(SnowflakeIdUtil.getSnowflakeId());
        group.setId(sonwflakeId);
        group.setRemark(addGroupVo.getRemark());
        group.setSubjectId(addGroupVo.getSubjectId());
        group.setSubjectsId(addGroupVo.getSubjectsId());
        group.setDocterId(addGroupVo.getDocterId());
        group.setRegisterDate(addGroupVo.getRegisterDate());
        group.setRegisterTime(addGroupVo.getRegisterTime());
        group.setStartTime(addGroupVo.getStartTime());
        group.setStartTimeStr(startTime);
        group.setGroupUserList(new ArrayList<GroupUser>());
        group.setIsActive(true);
        group.setTaskType(1);
        group.setCreateDate(createDate);
        group.setIsDelect(false);
        group.setOrderNumBer(0);
        Cache.setGroupMap(group);
        new Thread(new OrderThread(group.getId(),okHttpClient)).start();
        return 1;
    }

    @Override
    public int addUser(AddGroupUserVo addGroupUserVo) {
        String snowflakeId = String.valueOf(SnowflakeIdUtil.getSnowflakeId());
        Group group = Cache.getGroupMapById().get(addGroupUserVo.getGroupId());

        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String createDate = format.format(date);

        int addUserNumber = 0;

        for(IdObjVo userId : addGroupUserVo.getUserId()){
            for(User userFor : Cache.getUserList()){
                if(userFor.getCardId().equals(userId.getUserId())){
                    GroupUser groupUser = new GroupUser();
                    groupUser.setSnowflakeId(snowflakeId);
                    groupUser.setCardId(userFor.getCardId());
                    groupUser.setCookie(userFor.getCookie());
                    groupUser.setIsActive(userFor.getIsActive());
                    groupUser.setRemark(userFor.getRemark());
                    groupUser.setIsSnatch(true);
                    groupUser.setIsActive(userFor.getIsActive());
                    groupUser.setCreateDate(createDate);
                    groupUser.setUsage(0);
                    groupUser.setIsOrder(false);
                    groupUser.setUserName(userFor.getUserName());
                    groupUser.setHospitalUserId(userFor.getHospitalUserId());
                    group.getGroupUserList().add(groupUser);
                    addUserNumber++;
                }
            }
        }

       return addUserNumber < 1 ? 0 : addUserNumber;
    }

    @Override
    public List<GroupInsideUserDto> insideUser(String groupId) {
        Group group = Cache.getGroupMapById().get(groupId);

        List<GroupInsideUserDto> groupInsideUserDtoList = new ArrayList<>();
        for(GroupUser groupUser : group.getGroupUserList()){
                GroupInsideUserDto groupInsideUserDto =new GroupInsideUserDto();
                groupInsideUserDto.setGroupId(group.getId());
                groupInsideUserDto.setSnowflakeId(groupUser.getSnowflakeId());
                groupInsideUserDto.setCreateDate(groupUser.getCreateDate());
                groupInsideUserDto.setUsage(groupUser.getUsage());
                groupInsideUserDto.setCookie(groupUser.getCookie());
                groupInsideUserDto.setIsActive(groupUser.getIsActive());
                groupInsideUserDto.setIsSnatch(groupUser.getIsSnatch());
                groupInsideUserDto.setUserName(groupUser.getUserName());
                groupInsideUserDto.setIsOrder(groupUser.getIsOrder());
                groupInsideUserDtoList.add(groupInsideUserDto);
        }

        return groupInsideUserDtoList;
    }

    @Override
    public int delectGroupById(String groupId) {
        Group group = Cache.getGroupMapById().get(groupId);
        group.setIsDelect(true);
        group = Cache.getGroupMapById().remove(groupId);
        return group != null ? 1 : 0;
    }



    @Override
    public int stopGroupUserById(String groupId,String userId) {
        int result = 0;
        Group group = Cache.getGroupMapById().get(groupId);
        synchronized (group.getGroupUserList().iterator()){
            Iterator<GroupUser> groupUserIterator = group.getGroupUserList().iterator();
            while (groupUserIterator.hasNext()){
                GroupUser groupUser = groupUserIterator.next();
                if(groupUser.getSnowflakeId().equals(userId)){
                    groupUser.setIsSnatch(groupUser.getIsSnatch() ? false : true);
                    result = 1;
                }
            }
        }

        return result;
    }

    @Override
    public int delectGroupUserById(String groupId, String userId) {
        int result = 0;
        Group group = Cache.getGroupMapById().get(groupId);

        synchronized (group.getGroupUserList().iterator()){
            Iterator<GroupUser> groupUserIterator = group.getGroupUserList().iterator();
            while (groupUserIterator.hasNext()){
                if(groupUserIterator.next().getSnowflakeId().equals(userId)){
                    groupUserIterator.remove();
                    result = 1;
                }
            }
        }

        return result;
    }

    @Override
    public void delectGroupUserAllByCardId(String cardId) {
        for(Map.Entry<String,Group> entry : Cache.getGroupMapById().entrySet()){
             synchronized (entry.getValue().getGroupUserList().iterator()){
                 Iterator<GroupUser> groupUserIterator =  entry.getValue().getGroupUserList().iterator();
                 while (groupUserIterator.hasNext()){
                     GroupUser groupUser = groupUserIterator.next();
                     if(groupUser.getCardId().equals(cardId)){
                         groupUserIterator.remove();
                     }
                 }
             }
        }
    }
}
