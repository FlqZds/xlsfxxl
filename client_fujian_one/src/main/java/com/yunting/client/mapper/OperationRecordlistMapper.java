package com.yunting.client.mapper;

import com.yunting.client.DTO.RetainActive;
import com.yunting.client.entity.OperationRecordlist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("OperationRecordlistMapper")
public interface OperationRecordlistMapper {

//    插入一条修改记录
    int insert(OperationRecordlist record);

    List<OperationRecordlist> selectAllRecordByCondition(@Param("active") RetainActive active);

    // 根据id修改异常记录
    public Integer changeOperationRecord(@Param("operationId") Long operationId, @Param("operationMsg") String operationMsg);


}