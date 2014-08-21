package pl.info.rkluszczynski.image.standalone.db.app

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import pl.info.rkluszczynski.image.standalone.config.ApplicationJavaConfig
import pl.info.rkluszczynski.image.standalone.db.repositories.PlanogramRepository
import pl.info.rkluszczynski.image.standalone.db.repositories.ProcessedImageRepository

class ProcessedImageRepositoryTestApp {

    public static main(String[] args) {
        def context = new AnnotationConfigApplicationContext(ApplicationJavaConfig.class)
        def planogramRepository = context.getBean(PlanogramRepository.class)
        def processedImageRepository = context.getBean(ProcessedImageRepository.class)

        println "PLANOGRAM:"
        planogramRepository.findAll().each { planogramEntity ->
            System.out.println("##### " + planogramEntity)
        }

        println "PROCESSEDIMAGE:"
        processedImageRepository.findAll().each { imageEntity ->
            System.out.println("##### " + imageEntity)
        }

        context.close()
    }
}
