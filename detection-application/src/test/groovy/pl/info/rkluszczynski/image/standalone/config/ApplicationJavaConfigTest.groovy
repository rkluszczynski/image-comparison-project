package pl.info.rkluszczynski.image.standalone.config

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification

import javax.sql.DataSource

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class, loader = AnnotationConfigContextLoader.class)
//@ActiveProfiles(profiles = "dev")
class ApplicationJavaConfigTest extends Specification {

    @Autowired
    DataSource dataSource

    @Test
    void 'should autowire dataSource'() {
        dataSource != null
    }
}
