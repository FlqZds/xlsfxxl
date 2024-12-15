package com.yunting.client.DTO.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableVo {
    private String Name;
    private String Rows;
    private String DataLength;
    private String AutoIncrement;
    private String createTime;
    private String updateTime;
}
