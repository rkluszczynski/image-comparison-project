package pl.info.rkluszczynski.image.standalone.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.info.rkluszczynski.image.standalone.config.ApplicationConstants;
import pl.info.rkluszczynski.image.standalone.exception.StandaloneApplicationException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

@Component(value = "statisticsCalculator")
public class StatisticsCalculator implements StandaloneRunner {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsCalculator.class);

    @Autowired
    StatisticsCollector statisticsCollector;

    @Override
    public void run() throws StandaloneApplicationException {
        try {
            File baseSetDir = checkAndGetDirectoryFile(ApplicationConstants.BASE_SET_FILES);
            File compareSetDir = checkAndGetDirectoryFile(ApplicationConstants.COMPARE_SET_FILES);
            for (File baseFile : baseSetDir.listFiles()) {
                logger.info("Processing file: " + baseFile.getAbsolutePath());

                String filename = baseFile.getName();
                String basename = filename.substring(0, filename.lastIndexOf("."));

                FilenameFilter filenameFilter = createFilenameFilter(
                        String.format("%s-", basename),
                        String.format("%s_", basename)
                );
                for (File compareFile : compareSetDir.listFiles(filenameFilter)) {
                    String compareFilename = compareFile.getName();
                    logger.info(" -> comparing files {} with {}", filename, compareFilename);

                    statisticsCollector.process(baseFile, compareFile);
                }
            }
            statisticsCollector.saveAsCSV("statistics.csv");
        } catch (Exception e) {
            throw new StandaloneApplicationException("Something went wrong!", e);
        }
    }

    private FilenameFilter createFilenameFilter(final String... basenames) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                for (String basename : basenames) {
                    if (name.startsWith(basename))
                        return true;
                }
                return false;
            }
        };
    }

    private File checkAndGetDirectoryFile(String dirPath) throws IOException {
        File directoryFile = new File(dirPath);
        if (directoryFile.exists() && directoryFile.isDirectory()) {
            return directoryFile;
        }
        logger.warn(String.format("Current working directory: %s", System.getProperty("user.dir")));
        throw new IOException("Path " + dirPath + " not exists or is not directory");
    }
}
