import com.yunting.Screenshot.FileOneApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest(classes = FileOneApplication.class)
public class qwe {
    @Test
    void test() {
        LocalDateTime.parse("2024-06-14 15:48:08");
        Duration between = Duration.between(LocalDateTime.of(2024, 12, 17, 12, 0), LocalDateTime.now());
        System.out.println(between.toHours());
    }
}
