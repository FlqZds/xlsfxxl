
import com.yunting.adv.AdvOneApplication;
import com.yunting.adv.mapper.Adv.AdEncourageMapper;
import com.yunting.common.utils.RedisUtil_session;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

@SpringBootTest(classes = AdvOneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class qweqwefqwe {

    @Resource(name = "AdEncourageMapper")
    private AdEncourageMapper adEncourageMapper;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Test
    public void test() {

        System.out.println(rus.hGet("1000030", "wxName"));
        System.out.println(Objects.nonNull(rus.hGet("1000030", "wxName")));
//        adEncourageMapper.changeAdEncourageRecordClickTime("5000000000068", LocalDateTime.now(), 1);
//        char c = "100001".charAt(0);
//        System.out.println(c);
    }

}
