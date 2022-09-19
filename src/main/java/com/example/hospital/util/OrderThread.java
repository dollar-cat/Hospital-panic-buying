package com.example.hospital.util;

import com.alibaba.fastjson.JSONObject;
import com.example.hospital.data.Cache;
import com.example.hospital.dto.DetailedAccountDto;
import com.example.hospital.dto.DocterDto;
import com.example.hospital.dto.GetOrderDataDto;
import com.example.hospital.pojo.Group;
import com.example.hospital.pojo.GroupUser;
import javazoom.jl.player.advanced.AdvancedPlayer;
import okhttp3.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderThread implements Runnable{

    private static Group group;
    private static final String DOC_ORDER_API = "https://fwcbj.linkingcloud.cn/GuaHao/OrderDocNoSources"; //获取下单数据
    private static final String ALIPAY_API = "https://fwcbj.linkingcloud.cn/GuaHao/Alipay_RegistApply"; //下单接口
    private OkHttpClient okHttpClient;
    private OrderModel orderModel = new OrderModel();

    public OrderThread(String groupId,OkHttpClient okHttpClient){
       group = Cache.getGroupMapById().get(groupId);
       this.okHttpClient = okHttpClient;

        DetailedAccountDto detailedAccountDto = Cache.getDetailedAccountByTopId(group.getSubjectId());
        DocterDto docterDto = Cache.getDocterDtoById(group.getDocterId());
        orderModel.setUrl("/guahao/doctor");
        orderModel.setDeptCode(detailedAccountDto.getDeptCode());
        orderModel.setDeptName(detailedAccountDto.getDeptName());
        orderModel.setDocCode(docterDto.getDocCode());
        orderModel.setDocDuty(docterDto.getDocDuty());
        orderModel.setDocName(docterDto.getDocName());
    }

    @Override
    public void run() {
        int orderNumber = 0;
        try {
            while (true){
                if(group.getIsDelect()){
                    break;
                }
                if(System.currentTimeMillis() >= group.getStartTime()) {
                    if (group.getIsActive()) {
                        orderNumber = 0;
                        synchronized ( group.getGroupUserList().iterator()){
                            Iterator<GroupUser> groupUserIterator = group.getGroupUserList().iterator();
                            while (groupUserIterator.hasNext()){
                                GroupUser groupUser = groupUserIterator.next();
                                if (!groupUser.getIsOrder()) {
                                    if (groupUser.getIsActive()) {
                                        if (groupUser.getIsSnatch()) {
                                            List<GetOrderDataDto> getOrderDataDtoList = getOrderData(groupUser.getCookie());
                                            //遍历多个下单类型
                                            for (GetOrderDataDto getOrderDataDto : getOrderDataDtoList) {
                                                //如果订单日期是当天
                                                if (group.getRegisterDate().equals(getOrderDataDto.getDay())) {
                                                    //如果可下单
                                                    if (getOrderDataDto.getIsAvailable().equals("1")) {
                                                        orderModel.setHospitalUserId(groupUser.getHospitalUserId());
                                                        orderModel.setResourceId(getOrderDataDto.getResourceID());
                                                        orderModel.setRegistDate(getOrderDataDto.getDay() + " " + getOrderDataDto.getTimeEnd());
                                                        groupUser.setUsage(groupUser.getUsage() + 1);
                                                        //如果订单详细日期等于设定的
                                                        //if(group.getRegisterDate().equals(getOrderDataDto.getTimeEnd())){
                                                        boolean isOrder = order(groupUser.getCookie());
                                                        if (isOrder) {
                                                            groupUser.setIsOrder(true);
                                                        }
                                                        //如果不等于设定的 也下单
//                                         }else{
//                                             order(orderModel);
//                                         }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }else{
                                    orderNumber++;
                                }

                            }
                        }

                        group.setOrderNumBer(orderNumber);
                        if(group.getGroupUserList().size() == orderNumber){
                            group.setIsActive(false);
                        }
                    }
                }
                }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<GetOrderDataDto> getOrderData(String cookie){
        List<GetOrderDataDto> getOrderDataDtoList = null;
        try {
            Headers.Builder headersBuilder = new Headers.Builder();
            headersBuilder.add("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/3.3(0x13030013) NetType/WIFI WindowsWechat");
            headersBuilder.add("cookie",cookie);

            FormBody.Builder formBody = new FormBody.Builder();
            formBody.add("docCode",group.getDocterId());
            Request request = new Request.Builder()
                    .url(DOC_ORDER_API)
                    .headers(headersBuilder.build())
                    .post(formBody.build())
                    .build();

            Response response;
            String result = null;
            response = okHttpClient.newCall(request).execute();
            result = response.body().string();
            getOrderDataDtoList = new ArrayList();
            if(result != null){
                JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
                getOrderDataDtoList = JSONObject.parseArray(jsonObject.get("docResourceResourceList").toString(), GetOrderDataDto.class);
            }

            return getOrderDataDtoList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return getOrderDataDtoList;
    }

    public boolean order(String cookie){
        try {
            Headers.Builder headersBuilder = new Headers.Builder();
            headersBuilder.add("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/3.3(0x13030013) NetType/WIFI WindowsWechat");
            headersBuilder.add("cookie",cookie);

            FormBody formBody = new FormBody.Builder()
                    .add("hospitalUserID",orderModel.getHospitalUserId())
                    .add("resourceID",orderModel.getResourceId())
                    .add("registDate",orderModel.getRegistDate())
                    .add("url",orderModel.getUrl())
                    .add("docCode",orderModel.getDocCode())
                    .add("docName",orderModel.getDocName())
                    .add("docDuty",orderModel.getDocDuty())
                    .add("deptCode",orderModel.getDeptCode())
                    .add("deptName",orderModel.getDeptName())
                    .add("extInfo","{\"continueSubmit:true\"}")
                    .build();

            Request request = new Request.Builder()
                    .url(ALIPAY_API)
                    .post(formBody)
                    .headers(headersBuilder.build())
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            JSONObject result = (JSONObject) JSONObject.parse(response.body().string());
            result = JSONObject.parseObject(result.get("responseResult").toString());

            if(result.toJSONString().indexOf("订单生成成功") != -1){
                try {
                    FileInputStream fIn = new FileInputStream("./music.mp3");
                    BufferedInputStream bis = new BufferedInputStream(fIn);
                    AdvancedPlayer player = new AdvancedPlayer(bis);
                    player.play(); //播放音频文件
                } catch (Exception e) {

                }

                return true;
            }else if(result.toJSONString().indexOf("您有此科室医生待付费挂号记录,请到“我的挂号”中进行付费") != -1){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
