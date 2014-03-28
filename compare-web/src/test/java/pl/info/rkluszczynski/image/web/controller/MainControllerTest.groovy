package pl.info.rkluszczynski.image.web.controller

import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
class MainControllerTest extends Specification {

    private MockMvc mockMvc;

    @InjectMocks
    private MainController mainControllerUnderTest;

//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = standaloneSetup(mainControllerUnderTest)
//                .setViewResolvers(viewResolver())
//                .build();
//    }
//
//    @Test
//    public void testComparePathShouldForwards() throws Exception {
//        mockMvc.perform(get("/compare").accept(MediaType.TEXT_HTML))
//                .andExpect(status().isMovedTemporarily())
//                .andExpect(redirectedUrl("/compare/"));
//    }
//
//
//    private InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/templates");
//        viewResolver.setSuffix(".html");
//        return viewResolver;
//    }
}
