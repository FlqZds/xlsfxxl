package com.yunting.client.common.config.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fileinfo {
    private Boolean directory;
    private String filename;
    private String previewUrl;
    private List<String> objectsUrl;
}
