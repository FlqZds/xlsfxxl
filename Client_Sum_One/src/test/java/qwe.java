import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.sum.SumOneApplication;
import com.yunting.sum.mapper.AdnMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@SpringBootTest(classes = SumOneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class qwe {

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "ST")
    private ST st;


    @Resource(name = "AdnMapper")
    private AdnMapper adnMapper;

    @Test
    public void test() {
        List<String> OneProxyName = adnMapper.getAllProxyName();//一级

        List<String> sonName = adnMapper.getAllProxySonName();//二级

        List<String> allProxyName = new ArrayList<>();
        allProxyName.addAll(sonName);

        allProxyName.addAll(OneProxyName);
        //排序 ,将二级,也就是名称带_的排前面
        int head = 0;
        Collections.sort(allProxyName, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // 如果一个字符串包含"_"，另一个不包含"_"，则带"_"的排前面
                if (s1.contains("_") && !s2.contains("_")) {
                    return -1;
                } else if (!s1.contains("_") && s2.contains("_")) {
                    return 1;
                } else {
                    return s1.compareTo(s2);  // 如果都包含或都不包含"_"，则按字典顺序比较
                }
            }
        });
        System.out.println(allProxyName);
    }

    @Test
    public void test11234() {
        String inLine_user = "1000019_江山豪庭";
        String[] s = inLine_user.split("_");
        String playerID = s[0];
        String location = s[1];
        System.out.println(playerID);
        System.out.println(location);
    }

    @Autowired
    qwe(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;
    private boolean taskShouldRun = true;  // 控制任务是否执行的条件

    // 启动任务
    public void startTask() {
        taskShouldRun = true;
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            // 自定义执行逻辑
            scheduledFuture = taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {

                }
            }, new CronTrigger("0 5 12 * * ?"));
        }
    }

    // 停止任务
    public void stopTask() {
        taskShouldRun = false;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    // 执行任务的逻辑
    private void executeTask() {
        System.out.println("Scheduled task running at " + System.currentTimeMillis());
    }

    // 取消任务
    private void cancelTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

}
