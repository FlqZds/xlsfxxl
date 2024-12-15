package com.yunting.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMenuright {
    private Integer menuId;

    private String menuTitle;

    private String menuRouter;

    private String menuIcon;

}