package pl.info.rkluszczynski.image.standalone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.info.rkluszczynski.image.engine.config.EngineJavaConfig;

import javax.sql.DataSource;

import static pl.info.rkluszczynski.image.standalone.config.ApplicationConstants.DATABASE_BASE_REPOSITORIES_PACKAGE;
import static pl.info.rkluszczynski.image.standalone.config.ApplicationConstants.DETECTION_APPLICATION_BASE_PACKAGE;

@Configuration
@ComponentScan(basePackages = DETECTION_APPLICATION_BASE_PACKAGE)
@EnableJpaRepositories(basePackages = DATABASE_BASE_REPOSITORIES_PACKAGE)
@PropertySource(value = "classpath:detection-application.properties")
@Import(EngineJavaConfig.class)
public class EmbeddedDataSourceConfig {

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        return new EmbeddedMysqlDatabaseBuilder()
                .addSqlScript("mysql-test-scripts/create-evaluation-tables.sql")
                .addSqlScript("mysql-test-scripts/create-tables.sql")
                .addSqlScript("mysql-test-scripts/insert-data.sql")
                .build();
    }
}
