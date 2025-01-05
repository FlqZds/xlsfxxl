import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.TimeUtils;
import com.yunting.ws.WSoneApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = WSoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class qwe {

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "ST")
    private ST st;

    @Test
    public void test() {
        System.out.println(LocalDateTime.now().plusDays(-1));
    }

}
