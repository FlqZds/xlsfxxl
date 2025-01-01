import com.yunting.Screenshot.FileOneApplication;
import com.yunting.Screenshot.entity.ImageMapping;
import com.yunting.Screenshot.mapper.ImgorderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@SpringBootTest(classes = FileOneApplication.class)
public class qwe {

    @Resource(name = "ImgorderMapper")
    ImgorderMapper imgorderMapper;

    @Test
    void test() {
        ImageMapping imgInfo = imgorderMapper.selectImgByImginfo("3d365ebfe5f1b9e9d6e6c3539829ad0ddda092fd50f7622b49d75ddef2464e72", "SS_cf420129-d80d-489f-bf87-4cfe62c9843f");
        System.out.println(imgInfo.getImgType());
        System.out.println(imgInfo.getImgType().getClass());
        System.out.println(imgInfo.getImgType().equals("o"));
    }
}
