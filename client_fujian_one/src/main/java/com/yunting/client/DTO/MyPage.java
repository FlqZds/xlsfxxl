package com.yunting.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPage {
    public Integer pageSize;
    public Integer pageNumber;

    public List rows;
    public Long total;

    public MyPage(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }


    public MyPage(Integer pageSize,Integer pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
