package pl.info.rkluszczynski.image.compare

import spock.lang.Specification

import javax.imageio.ImageIO

/**
 * Created by Rafal on 2014-06-14.
 */
class ImageDifferTest extends Specification {

    def "should ImageDiffer calculate statistics"() {
        when:
        println System.getProperty("user.dir")
        def dir = "src/test/resources"

        def img1 = ImageIO.read(new File(dir + "/happy-face-scaled.png"))
        def img2 = ImageIO.read(new File(dir + "/happy-face-rotated.png"))

        ImageDiffer.calculateDifferStatistics(img1, img2)

        then:
        true
    }
}
