import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yunting.FJOneApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FJOneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AlgorithemTest {


    @Test
    void test2() {

        String packageName = "{\"packageName\"=\"com.layabox.gameyzzs\"}";
        JsonObject asJsonObject = JsonParser.parseString(packageName).getAsJsonObject();
        packageName = asJsonObject.get("packageName").toString();
        log.info(packageName + "");
    }
}
