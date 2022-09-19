package com.example.hospital.util;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpRequ {

    @Autowired
    static OkHttpClient okHttpClient;

    public static

    @Value(value = "${userApi}")
    String userApi;
    @Value(value = "${ua}")
    String ua;

}
