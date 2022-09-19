package com.example.hospital.config;

import com.example.hospital.data.Cache;
import com.example.hospital.pojo.User;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CronConfig {
    @Autowired
    OkHttpClient okHttpClient;

    @Scheduled(fixedDelay = 600*1000)
    public void survivalCookie(){
        try {
            for(User user : Cache.getUserList()){
                Headers.Builder headersBuilder = new Headers.Builder();
                headersBuilder.add("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/3.3(0x13030013) NetType/WIFI WindowsWechat");
                headersBuilder.add("cookie",user.getCookie());

                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("dataSource","");

                Request request = new Request.Builder()
                        .url("https://fwcbj.linkingcloud.cn/GuaHao/OrderDeptResources")
                        .headers(headersBuilder.build())
                        .post(formBody.build())
                        .build();

                okHttpClient.newCall(request).execute();
                Thread.sleep(10000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
