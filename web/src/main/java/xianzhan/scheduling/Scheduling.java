package xianzhan.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xianzhan
 * @since 2018-05-15
 */
@Component
public class Scheduling {

    /**
     * @apiNote http://cron.qqe2.com/
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void echo() {
        System.out.println(new Date());
    }
}
