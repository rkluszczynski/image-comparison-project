package pl.info.rkluszczynski.image.standalone.runner;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class StatisticsCollector {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);

    private List<String> entryColNames = Lists.newArrayList("baseFilename", "compareFilename");
    private List<StatisticsEntry> entryList = Lists.newArrayList();

    void process(File baseFile, File compareFile) throws IOException {
        BufferedImage baseImage = ImageIO.read(baseFile);
        BufferedImage compareImage = ImageIO.read(compareFile);

        StatisticsEntry entry = createStatisticsEntry(
                baseFile.getName(),
                compareFile.getName()
        );
        List<Double> stats = Lists.newArrayList();
        ImageDiffer.calculateDifferStatistics(baseImage, compareImage,
                entryColNames.size() == 2 ? entryColNames : null, stats);
        entry.addStatistics(stats);
    }

    private StatisticsEntry createStatisticsEntry(String baseFileName, String compareFileName) {
        StatisticsEntry entry = new StatisticsEntry(baseFileName, compareFileName);
        entryList.add(entry);
        return entry;
    }

    public void saveAsCSV(String filename) throws IOException {
        Path path = Paths.get(filename);
        String csvHeader = StringUtils.collectionToDelimitedString(entryColNames, ",");
        List<String> lines = Lists.newArrayList(csvHeader);
        lines.addAll(Collections2.transform(entryList, new Function<StatisticsEntry, String>() {
            @Override
            public String apply(StatisticsEntry entry) {
                return entry.toString();
            }
        }));
        Files.write(path, lines, StandardCharsets.UTF_8);
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
