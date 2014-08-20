package pl.info.rkluszczynski.image.standalone.config;

import com.mysql.management.MysqldResource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;

/*
    Based on awesome post:

    http://zhentao-li.blogspot.com/2013/06/using-embedded-mysql-database-for-unit.html
 */
public class EmbeddedMysqlDatabase extends DriverManagerDataSource {
    private final Logger logger = LoggerFactory.getLogger(EmbeddedMysqlDatabase.class);
    private final MysqldResource mysqldResource;

    public EmbeddedMysqlDatabase(MysqldResource mysqldResource) {
        this.mysqldResource = mysqldResource;
    }

    public void shutdown() {
        if (mysqldResource != null) {
            mysqldResource.shutdown();
            if (!mysqldResource.isRunning()) {
                logger.info(">>>>>>>>>> DELETING MYSQL BASE DIR [{}] <<<<<<<<<<", mysqldResource.getBaseDir());
                try {
                    FileUtils.forceDelete(mysqldResource.getBaseDir());
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
