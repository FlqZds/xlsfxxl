
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.TimeUtils;
import com.yunting.pay.PayOneApplication;
import com.yunting.pay.mapper.PlayerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = PayOneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class qweqwefqwe {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "ST")
    private ST st;

    @Test
    public void test() {
        rur.hPut("t", "t", "t");
        rur.expire("t", 1, TimeUnit.MINUTES);
    }
}
