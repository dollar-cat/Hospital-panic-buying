package com.example.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.hospital.data.Cache;
import com.example.hospital.dto.DetailedAccountDto;
import com.example.hospital.dto.DocterDto;
import com.example.hospital.dto.SubjectDto;
import com.example.hospital.service.DocterService;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DocterServiceImpl implements DocterService {

    @Autowired
     OkHttpClient okHttpClient;

    @Value(value = "${docterApi}")
    String docterApi;

    @Override
    public int initData() throws IOException {
        //获取个cookie 用于获取医生列表
        String cookie = null;
        for(Map.Entry<String,String> entries : Cache.getCookieMap().entrySet()){
            cookie = entries.getKey();
        }

        Headers.Builder headersBuilder = new Headers.Builder();
        headersBuilder.add("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/3.3(0x13030013) NetType/WIFI WindowsWechat");
        headersBuilder.add("cookie",cookie);

        Request request = new Request.Builder()
                .url(docterApi)
                .get()
                .headers(headersBuilder.build())
                .build();

        Response response = okHttpClient.newCall(request).execute();

        JSONObject objStr = (JSONObject) JSONObject.parse(response.body().string());
        //科
        List<SubjectDto> subjectDtoList = JSONObject.parseArray(objStr.get("deptLevel1List").toString(), SubjectDto.class);
        //详细科
        List<DetailedAccountDto> detailedAccountDtoList = JSONObject.parseArray(objStr.get("deptLevel2List").toString(), DetailedAccountDto.class);
        //各科医生
        List<DocterDto> docterDtoList = JSONObject.parseArray(objStr.get("deptResourceDocList").toString(), DocterDto.class);

        Cache.setSubjectDtoList(subjectDtoList);
        Cache.setDetailedAccountMap(detailedAccountDtoList);
        Cache.setDocterMap(docterDtoList);
        Cache.setDocterList(docterDtoList);

        if(subjectDtoList == null || detailedAccountDtoList == null || docterDtoList == null){
            return 0;
        }

        return 1;
    }
}
