package pl.info.rkluszczynski.image.engine

import pl.info.rkluszczynski.image.config.ImageCoreTestConfig
import spock.lang.Specification

import javax.imageio.ImageIO

/**
 * Created by Rafal on 2014-06-14.
 */
class ImageDifferTest extends Specification {

    def "should ImageDiffer calculate statistics"() {
        when:
        def dir = ImageCoreTestConfig.getTestResourcesDirectory()
        println dir
        dir += '/src/test/resources/'

        def img1 = ImageIO.read(new File(dir + 'happy-face-scaled.png'))
        def img2 = ImageIO.read(new File(dir + 'happy-face-rotated.png'))

        ImageDiffer.calculateDifferStatistics(img1, img2)

        then:
        true
    }
}
