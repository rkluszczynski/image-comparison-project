package pl.info.rkluszczynski.image.engine.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SessionCacheCleanerTask {

    private static Logger logger = LoggerFactory.getLogger(SessionCacheCleanerTask.class);

    private static final long HALF_AN_HOUR_IN_MS = 30L * 60L * 1000L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(initialDelay = HALF_AN_HOUR_IN_MS, fixedRate = HALF_AN_HOUR_IN_MS)
    public void reportCurrentTime() {
        logger.info("{} executed at time: {}", getClass().getName(), dateFormat.format(new Date()));
    }
}
