import com.alipay.api.domain.Participant;
import com.yunting.FJOneApplication;
import com.yunting.client.common.utils.AliPayUtil;
import com.yunting.client.common.utils.MinIoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(classes = FJOneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AlgorithemTest {
    @Autowired
    private MinIoUtils minIoUtils;

    @Test
    void test2() {

        log.info("ok");
    }
}
