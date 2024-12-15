package com.yunting.client.mapper;

import com.yunting.client.DTO.RetainActive;
import com.yunting.client.entity.ExceptionRecordlsit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ExceptionRecordlsitMapper")
public interface ExceptionRecordlsitMapper {

    //    插入一条异常记录
    public Integer insertExceptionRecord(ExceptionRecordlsit exceptionRecordlsit);

    //    查到所有异常记录
    public List<ExceptionRecordlsit> getAllExceptionRecord(RetainActive active);


}