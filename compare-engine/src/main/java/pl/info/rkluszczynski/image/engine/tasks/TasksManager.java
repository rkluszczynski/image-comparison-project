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
@Qualifier("imageTasksManager")
public class TasksManager {
    private static Logger logger = LoggerFactory.getLogger(TasksManager.class);

    private static final long HALF_AN_HOUR_IN_MS = 30L * 60L * 1000L;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    private List<AbstractTask> abstractTasksList = Lists.newArrayList();


    public void submitImageTask(Runnable imageProcessingTask) {
        try {
            abstractTasksList.add((AbstractTask) imageProcessingTask);
            taskExecutor.execute(imageProcessingTask);
        } catch (Exception e) {
            logger.info("Problem with task submission", e);
        }
    }

    @Scheduled(initialDelay = HALF_AN_HOUR_IN_MS, fixedRate = HALF_AN_HOUR_IN_MS)
    public void performExecutorsCleanUp() {
        Set<AbstractTask> tasksToClean = Sets.newHashSet();

        logger.info("Executing executors clean up");
        for (AbstractTask abstractTask : abstractTasksList) {
            HttpSession session = abstractTask.getSessionData().getSession();
            String sessionUniqueKey = abstractTask.getSessionData().getDataUniqueKey();

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
                abstractTask.interrupt();

                tasksToClean.add(abstractTask);
                logger.debug("Session {} invalidated and task interrupted and cleaned", sessionUniqueKey);
            }
        }

        abstractTasksList.removeAll(tasksToClean);
        logger.debug("Executors clean up done");
    }
}
