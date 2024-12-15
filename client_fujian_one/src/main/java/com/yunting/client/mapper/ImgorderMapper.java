package com.yunting.client.mapper;

import com.yunting.client.DTO.img.ImgShow;
import com.yunting.client.entity.ImageMapping;
import com.yunting.client.entity.Imgorder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ImgorderMapper")
public interface ImgorderMapper {
    //生成一笔订单
    int insertOneOrder(Imgorder record);

    //查到该玩家所有未通过('i')的图片
    List<ImgShow> selectPlayerOrderNoURl(@Param("playerID") Long playerID, @Param("imgType") String imgType);

    //通过hash值和文件名称找到该图
    ImageMapping selectImgByImginfo(@Param("hashVal") String hashVal, @Param("fileName") String fileName);

    //通过文件名称找到该图
    ImageMapping selectImgByName(@Param("fileName") String fileName);

    //更新该笔订单奖励状态
    int updateOrderStatus(@Param("orderID") Long orderID, @Param("isGet") String isGet);
}