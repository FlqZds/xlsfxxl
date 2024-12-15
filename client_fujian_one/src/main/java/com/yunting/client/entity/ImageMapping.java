package com.yunting.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageMapping {
    private Long imgId;

    private String imgUrl;

    private String directory;

    private String fileName;

    private String fileHash;

    private String imgType;

    private Long imgOrderId;

    private Long playerId;

    private LocalDateTime uploadTime;

}