package pl.info.rkluszczynski.image.standalone.runner;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.info.rkluszczynski.image.engine.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class StatisticsCollector {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);

    private List<StatisticsEntry> entryList = Lists.newArrayList();

    void process(File baseFile, File compareFile) throws IOException {
        BufferedImage baseImage = ImageIO.read(baseFile);
        BufferedImage compareImage = ImageIO.read(compareFile);

        StatisticsEntry entry = createStatisticsEntry(
                baseFile.getName(),
                compareFile.getName()
        );
        List<Double> stats = Lists.newArrayList();
        ImageDiffer.calculateDifferStatistics(baseImage, compareImage, stats);
        entry.addStatistics(stats);
    }

    private StatisticsEntry createStatisticsEntry(String baseFileName, String compareFileName) {
        StatisticsEntry entry = new StatisticsEntry(baseFileName, compareFileName);
        entryList.add(entry);
        return entry;
    }

    public void saveAsCSV(String filename) {
        Path path = Paths.get(filename);
//        String[] statsLines = Collections2.transform(entryList, new Function() {
//
//        }
//        );
//
//        void writeSmallTextFile(List<String> aLines, String aFileName) throws IOException {
//            Path path = Paths.get(aFileName);
//            Files.write(path, aLines, ENCODING);
//        }
    }


    class StatisticsEntry {
        private final String baseFilename;
        private final String compareFilename;
        private final List<Double> statisticList;

        StatisticsEntry(String baseFilename, String compareFilename) {
            this.baseFilename = baseFilename;
            this.compareFilename = compareFilename;
            statisticList = Lists.newArrayList();
        }

        void addStatistics(List<Double> statistics) {
            statisticList.addAll(statistics);
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s", baseFilename, compareFilename,
                    StringUtils.collectionToDelimitedString(statisticList, ","));
        }
    }
}