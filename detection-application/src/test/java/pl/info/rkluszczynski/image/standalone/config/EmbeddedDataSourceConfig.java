package pl.info.rkluszczynski.image.standalone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class EmbeddedDataSourceConfig {

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        return new EmbeddedMysqlDatabaseBuilder()
                .addSqlScript("mysql-test-scripts/create-tables.sql")
//                .addSqlScript("tag_schema.sql")
//                .addSqlScript("tag_init.sql")
                .build();
    }
}
