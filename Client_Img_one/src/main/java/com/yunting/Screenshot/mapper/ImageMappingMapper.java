package com.yunting.Screenshot.mapper;

import com.yunting.Screenshot.entity.ImageMapping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("ImageMappingMapper")
public interface ImageMappingMapper {

    //    插入一条图片记录,订单截图才有该信息(和minio相关联上)
    int insertWithTrans(ImageMapping record);

    //    插入一条图片记录(和minio相关联上)
    int insert(ImageMapping record);

    //  根据图片id,修改图片的url,指定该图属于哪笔订单
    int updateByPrimaryKey(@Param("imgID") Long imdID, @Param("img") String url, @Param("imgOrderID") Long imgOrderID);

    //    根据id删除该图片记录
    int deleteByPrimaryKey(Long id);

    //根据图片类型 + 图片订单号 + 图片上传时间 找当日是否存在该笔订单号的图片数据
    int selectImgByImgTypeAndTransidAndUploadTime(@Param("imgType") String imgType, @Param("transID") String transID, @Param("uploadTime") LocalDateTime uploadTime,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);

    ImageMapping selectImgByDir(@Param("dir") String dir, @Param("fileName") String fileName);

}