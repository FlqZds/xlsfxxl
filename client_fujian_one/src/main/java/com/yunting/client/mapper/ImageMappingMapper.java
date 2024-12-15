package com.yunting.client.mapper;

import com.yunting.client.entity.ImageMapping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("ImageMappingMapper")
public interface ImageMappingMapper {

    //    插入一条图片记录(和minio相关联上)
    int insert(ImageMapping record);

    //  根据图片id,修改图片的url,指定该图属于哪笔订单
    int updateByPrimaryKey(@Param("imgID") Long imdID, @Param("img") String url, @Param("imgOrderID") Long imgOrderID);

    //    根据id删除该图片记录
    int deleteByPrimaryKey(Long id);


    ImageMapping selectImgByDir(@Param("dir") String dir, @Param("fileName") String fileName);

}