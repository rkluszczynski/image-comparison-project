package pl.info.rkluszczynski.image.standalone.config;

import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class ApplicationTestConfig extends ApplicationJavaConfig {

    @Override
    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
//        return super.dataSource();
        return new EmbeddedMysqlDatabaseBuilder()
                .addSqlScript("mysql-test-scripts/create-tables.sql")
//                .addSqlScript("tag_schema.sql")
//                .addSqlScript("tag_init.sql")
                .build();
    }
}
