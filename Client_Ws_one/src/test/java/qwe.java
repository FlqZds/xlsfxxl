import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.ws.WSoneApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = WSoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class qwe {

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Test
    public void test() {
        String inLine_user = rur.get("withdraw" + 1000030);
        int i = Integer.parseInt(inLine_user);
        System.out.println(i);
    }

}
