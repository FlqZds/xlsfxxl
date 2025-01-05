
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

        //关闭了禁止 ,则周末也关闭了 ,就是直接任何时间段都可以看广告
        //开启了禁止 ,就是每天的该时间段不可以看广告
        //如果周末也开了,就是周末全天都可以看广告
        //该时间段可以提现 ,提现的激励广告 不受此影响

//        if (st.isForbid_Switch() == false) { //关闭了禁止    //任何时间段都可以看广告
//            rur.set("isSeeAdv", "true");
//        } else {
//            //周末也开了,就是周末全天都可以看广告
//            if (st.IS_Weekend()) {
//                if (LocalDate.now().getDayOfWeek().getValue() == 6 || LocalDate.now().getDayOfWeek().getValue() == 7) {
//                    rur.set("isSeeAdv", "true");
//                }
//            }
//            //每天的该时间段不可以看广告
//            if (LocalDateTime.now().withHour(st.Forbid_Begin_Time()).isBefore(LocalDateTime.now()) &&
//                    LocalDateTime.now().withHour(st.Forbid_End_Time()).isAfter(LocalDateTime.now())) {
//                rur.set("isSeeAdv", "false");
//            }
//        }
        int a = 0;
        long tIimeStamp = TimeUtils.getTimeStamp();
//        long thisTimeStamp = TimeUtils.getThisTimeStamp(st.Forbid_Begin_Time(),0);
//        System.out.println(thisTimeStamp);
        System.out.println(tIimeStamp);
        long dayOfWeek = TimeUtils.getDayOfWeek();
        System.out.println(dayOfWeek);
    }
}
