package com.example.hospital.data;

import com.example.hospital.dto.DetailedAccountDto;
import com.example.hospital.dto.DocterDto;
import com.example.hospital.dto.GroupDto;
import com.example.hospital.dto.SubjectDto;
import com.example.hospital.pojo.Group;
import com.example.hospital.pojo.GroupUser;
import com.example.hospital.pojo.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Cache {
    private static Map<String,User> userMap = new ConcurrentHashMap<>();
    private static Map<String,String> userIdMapKcookie = new ConcurrentHashMap<>();
    private static List<SubjectDto> subjectDtoList = new Vector<>(); //科数据
    private static Map<String, List<DetailedAccountDto>> detailedAccountMap = new ConcurrentHashMap<>(); //详细科
    private static Map<String, List<DocterDto>> docterMap = new ConcurrentHashMap<>(); //详细科 医生
    private static List<DocterDto> docterDtoList = new Vector<>(); //医生
    private static Map<String, Group> groupMapById = new ConcurrentHashMap<>(); //组

    //通过医生id获取
    public static DocterDto getDocterDtoById(String id){
        AtomicReference<DocterDto> docterDto = new AtomicReference<>();
        docterDtoList.forEach(v -> {
            if(id.equals(v.getDocCode())){
                 docterDto.set(v);
            }
        });
        return docterDto.get();
    }

    //通过科id获取
    public static DetailedAccountDto getDetailedAccountByTopId(String subjectId){
        AtomicReference<DetailedAccountDto> detailedAccountDto = new AtomicReference<>();
        detailedAccountMap.get(subjectId).forEach(v -> {
            if(subjectId.equals(v.getParentDeptCode())){
                detailedAccountDto.set(v);
            }
        });

        return detailedAccountDto.get();
    }

    public static Map<String, Group> getGroupMapById(){
        return groupMapById;
    }

    public static void setDocterList(List<DocterDto> docterList){
        Cache.docterDtoList = docterList;
    }

    public static List<GroupDto> getGroupListDtoList(){
        List<GroupDto> groupListDtoList = new ArrayList<>();
        for(Map.Entry<String,Group> entry : groupMapById.entrySet()){

            Group group = entry.getValue();

             //获取医生名
             String docterName = null;
             for(DocterDto docterDto : docterDtoList){
                 if(docterDto.getDocCode().equals(entry.getValue().getDocterId())){
                     docterName = docterDto.getDocName();
                 }
             }

            String registerTime = null;
            switch (group.getRegisterTime()){
                case 0:
                    registerTime = "上午";
                    break;
                case 1:
                    registerTime = "下午";
                    break;
                case 2:
                    registerTime = "前夜";
                    break;
            }

             GroupDto groupDto = new GroupDto();
             groupDto.setId(group.getId());
             groupDto.setRemark(group.getRemark());
             groupDto.setDocter(docterName);
             groupDto.setCreateDate(group.getCreateDate().replaceAll("年|月","-").replaceAll("日",""));
             groupDto.setTaskStartDate(group.getStartTimeStr().replaceAll("年|月","-").replaceAll("日",""));
             groupDto.setRegisterDate(group.getRegisterDate().replaceAll("年|月","-").replaceAll("日","")+" "+registerTime);
             groupDto.setIsActive(group.getIsActive());
             groupDto.setTaskType(group.getTaskType());
             groupDto.setOrderNumber(group.getOrderNumBer());
             groupDto.setUserSize(group.getGroupUserList() == null ? 0 : group.getGroupUserList().size());
             groupDto.setActiveUserSize(0);
             for(GroupUser groupUser : group.getGroupUserList()){
                 if(groupUser.getIsSnatch() && groupUser.getIsActive()){
                     groupDto.setActiveUserSize(groupDto.getActiveUserSize()+1);
                 }
             }

             groupListDtoList.add(groupDto);
        }
        return groupListDtoList;
    }

    public static void setGroupMap(Group group){
        groupMapById.put(group.getId(),group);
    }

    public static List<SubjectDto> getSubjectDtoList(){
        return subjectDtoList;
    }

    public static List<DetailedAccountDto> getDetailedAccount(String id){
        return Cache.detailedAccountMap.get(id);
    }

    public static List<DocterDto> getDocterList(String id) {
        return Cache.docterMap.get(id);
    }

    public static List<DocterDto> getDocterMap(String id){
        return Cache.docterMap.get(id);
    }

    public static void setSubjectDtoList(List<SubjectDto> subjectDtoList){
        Cache.subjectDtoList = subjectDtoList;
    }

    public static void setDetailedAccountMap(List<DetailedAccountDto> detailedAccountDtoList){

        for(SubjectDto subjectDto : subjectDtoList){
            List<DetailedAccountDto> addData = new ArrayList<>();
            for(DetailedAccountDto detailedAccountDto : detailedAccountDtoList){
                if(subjectDto.getDeptCode().equals(detailedAccountDto.getParentDeptCode())){
                    addData.add(detailedAccountDto);
                }
            }
            Cache.detailedAccountMap.put(subjectDto.getDeptCode(),addData);
        }
    }

    public static void setDocterMap(List<DocterDto> docterDtoList){
        for(Map.Entry<String, List<DetailedAccountDto>> entry : detailedAccountMap.entrySet()){
            for(DetailedAccountDto detailedAccountDto : entry.getValue()){
                List<DocterDto> addData = new ArrayList<>();

                for(DocterDto docterDto : docterDtoList){
                    if(detailedAccountDto.getDeptCode().equals(docterDto.getDeptCode())){
                        addData.add(docterDto);
                    }
                }
                docterMap.put(detailedAccountDto.getDeptCode(),addData);
            }

        }

    }


    public static boolean putUser(User user){
        if(userMap.get(user.getCardId()) == null){
            userMap.put(user.getCardId(),user);
            return true;
        }

        return false;
    }

    public static boolean putCookie(String cookie,String userId){
        if(userIdMapKcookie.get(cookie) == null){
            userIdMapKcookie.put(cookie,userId);
            return true;
        }

        return false;
    }

    public static Map<String,String> getCookieMap(){
        return userIdMapKcookie;
    }

    public static List<User> getUserList() {
        List<User> userList = new ArrayList<>();

        for (Map.Entry<String, User> entry : userMap.entrySet()){
             userList.add(entry.getValue());
        }

        return userList;
    }

    public static Integer removeUser(String cardId) {
       return userMap.remove(cardId) != null ? 1 : 0;
    }

    public static Integer stopUser(String cardId) {
        User user = userMap.get(cardId);

        if(user == null){
            return 0;
        }

        if(user.getIsActive()){
            user.setIsActive(false);
        }else{
            user.setIsActive(true);
        }

        return  1;
    }

}
