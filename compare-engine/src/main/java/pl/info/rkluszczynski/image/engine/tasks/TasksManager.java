package pl.info.rkluszczynski.image.engine.tasks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("detectorTasksManager")
public class TasksManager {
    private static final Logger logger = LoggerFactory.getLogger(TasksManager.class);

    private static final long HALF_AN_HOUR_IN_MS = 30L * 60L * 1000L;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    private final List<AbstractDetectorTask> detectorTasksList = Lists.newArrayList();


    public void submitDetectorTask(Runnable imageDetectorTask) {
        try {
            detectorTasksList.add((AbstractDetectorTask) imageDetectorTask);
            taskExecutor.execute(imageDetectorTask);
        } catch (Exception e) {
            logger.info("Problem with task submission", e);
        }
    }

    @Scheduled(initialDelay = HALF_AN_HOUR_IN_MS, fixedRate = HALF_AN_HOUR_IN_MS)
    public void performExecutorsCleanUp() {
        Set<AbstractDetectorTask> tasksToClean = Sets.newHashSet();

        logger.info("Executing executors clean up");
        for (AbstractDetectorTask detectorTask : detectorTasksList) {
            HttpSession session = detectorTask.getSessionData().getSession();
            String sessionUniqueKey = session.getId();

            boolean taskCleanMarker = false;
            try {
                long lastAccessedTime = session.getLastAccessedTime();
                int maxInactiveInterval = session.getMaxInactiveInterval();
                long now = Calendar.getInstance().getTimeInMillis();
                if (now > lastAccessedTime + maxInactiveInterval) {
                    session.invalidate();
                    taskCleanMarker = true;
                }
            } catch (IllegalStateException ex) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Accessing session {} raises IllegalStateException (is it already invalidated?)",
                            sessionUniqueKey, ex);
                } else {
                    logger.warn("Accessing session {} which is probably invalid", sessionUniqueKey);
                }
                taskCleanMarker = true;
            }

            if (taskCleanMarker) {
                detectorTask.interrupt();

                tasksToClean.add(detectorTask);
                logger.debug("Session {} invalidated and task interrupted and cleaned", sessionUniqueKey);
            }
        }

        detectorTasksList.removeAll(tasksToClean);
        logger.debug("Executors clean up done");
    }
}
