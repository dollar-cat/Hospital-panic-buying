package com.example.hospital.config;

import com.example.hospital.utils.SSLSocketClientUtil;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OKHttpConfig {

    private final static Integer connectTimeout = 5;
    private final static Integer writeTimeout = 5;
    private final static Integer readTimeout = 5;

    @Bean
    public OkHttpClient setOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClientUtil.getSSLSocketFactory(), SSLSocketClientUtil.getX509TrustManager())
                .hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier())
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout,TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }
}
