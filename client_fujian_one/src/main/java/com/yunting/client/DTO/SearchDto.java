package com.yunting.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    private String advType=""; //广告类型
    private String selectOption="";  //选择框
    private String search="";  //输入框
    private String startTime;  //开始日期
    private String endTime; //结束日期
    private String selectAdn=""; //广告位
    private String selectPoint=""; //点击量
    private String selectEncourage=""; //激励广告的一些选择条件

    private Integer pageNumber=1; //页码
    private Integer pageSize=10;  //该页数量

}

