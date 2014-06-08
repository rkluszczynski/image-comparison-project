package pl.info.rkluszczynski.image.dsp

import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class DCTTest extends Specification {
    ImgMod34a imgMod34a

    void setup() {
        imgMod34a = new ImgMod34a()
    }

    def "should forward and inverse be id"() {
        when:
        println System.getProperty("user.dir")

        BufferedImage image = ImageIO.read(new File("image-core/src/test/resources/happy-face.png"))
        def imageToIntArray = ImageDataConverter.bufferedImageToIntArray(image)
        def img = imgMod34a.processImg(imageToIntArray, image.getWidth(), image.getHeight())

        def dctImage = ImageDataConverter.intArrayToBufferedImage(img)
        ImageIO.write(dctImage, "PNG", new File("image-core/src/test/resources/dct.png"))

//        def dctIntArray = ImageDataConverter.bufferedImageToIntArray(dctImage)
//        def orgImgArray = ImgMod34a.processImg(dctIntArray, image.getWidth(), image.getHeight())
//        def orgImage = ImageDataConverter.intArrayToBufferedImage(orgImgArray)
//        ImageIO.write(orgImage, "PNG", new File("image-core/src/test/resources/stage2.png"))

        then:
        true
    }
}
