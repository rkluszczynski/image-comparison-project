package pl.info.rkluszczynski.image.engine.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;


@EnableScheduling
public class SessionCacheScheduler {

    private static Logger logger = LoggerFactory.getLogger(SessionCacheScheduler.class);

    private static final long HALF_AN_HOUR_IN_MS = 30L * 60L * 1000L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedDelay = HALF_AN_HOUR_IN_MS, fixedRate = HALF_AN_HOUR_IN_MS)
    public void reportCurrentTime() {
        logger.info("{} executed at time: {}", getClass().getName(), dateFormat.format(new Date()));
    }
}
