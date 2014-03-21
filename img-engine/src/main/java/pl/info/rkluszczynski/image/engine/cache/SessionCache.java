package pl.info.rkluszczynski.image.engine.cache;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;


@Component
public class SessionCache {

    private static Logger logger = LoggerFactory.getLogger(SessionCache.class);

    private static final long HALF_AN_HOUR_IN_MS = 30L * 60L * 1000L;

    private Map<String, SessionData> sessionDataCache = Maps.newConcurrentMap();


    public void addSessionData(SessionData sessionData) {
        sessionDataCache.put(sessionData.getDataUniqueKey(), sessionData);
    }

    public SessionData getSessionData(String sessionDataKey) {
        if (! sessionDataCache.containsKey(sessionDataKey)) {
            return null;
        }
        return sessionDataCache.get(sessionDataKey);
    }

    @Scheduled(initialDelay = HALF_AN_HOUR_IN_MS, fixedRate = HALF_AN_HOUR_IN_MS)
    public void executeCacheCleanUp() {
        Set<String> sessionKeysToClean = Sets.newHashSet();

        logger.info("Executing cache clean up");
        for (Map.Entry<String, SessionData> sessionDataEntry : sessionDataCache.entrySet()) {
            String sessionDataKey = sessionDataEntry.getKey();

            HttpSession session = sessionDataEntry.getValue().getSession();
            long lastAccessedTime = session.getLastAccessedTime();
            int maxInactiveInterval = session.getMaxInactiveInterval();
            long now = Calendar.getInstance().getTimeInMillis();
            if (now > lastAccessedTime + maxInactiveInterval) {
                session.invalidate();
                sessionKeysToClean.add(sessionDataKey);
                logger.debug("Session key {} marked for clean", sessionDataKey);
            }
        }

        for (String sessionDataKey : sessionKeysToClean) {
            sessionDataCache.remove(sessionDataKey);
        }
        logger.debug("Cache clean up done");
    }
}
