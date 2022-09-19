package com.example.hospital.controller;

import com.example.hospital.data.Cache;
import com.example.hospital.dto.SubjectDto;
import com.example.hospital.resp.RespBean;
import com.example.hospital.resp.RespBeanEnum;
import com.example.hospital.service.DocterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/docter")
public class DocterController {

    @Autowired
    DocterService docterService;

    @GetMapping("/get")
    public RespBean get() throws IOException {
        int result = docterService.initData();
        return result < 1 ? RespBean.error(RespBeanEnum.GET_DOCTER_ERROR) : RespBean.success();
    }

    @GetMapping("/subject")
    public RespBean subject(){
        List<SubjectDto> subjectDtoList = Cache.getSubjectDtoList();
        return RespBean.success(subjectDtoList);
    }

    @GetMapping("/subjects/{id}")
    public RespBean subjectsList(@PathVariable String id){
        return RespBean.success(Cache.getDetailedAccount(id));
    }

    @GetMapping("/docter/{id}")
    public RespBean docterList(@PathVariable String id){
        return RespBean.success(Cache.getDocterList(id));
    }

}
