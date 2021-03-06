package pl.info.rkluszczynski.image.core.dsp

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Created by Rafal on 2014-06-08.
 */
class DCTApp {

    public static void main(String[] args) {
        println System.getProperty("user.dir")
        def imgMod34a = new ImgMod34a()

        BufferedImage image = ImageIO.read(new File("image-core/src/test/resources/happy-face-scaled.png"))
        def imageToIntArray = ImageDataConverter.bufferedImageToIntArray(image)
        def img = imgMod34a.processImg(imageToIntArray, image.getWidth(), image.getHeight())

        def dctImage = ImageDataConverter.intArrayToBufferedImage(img)
        ImageIO.write(dctImage, "PNG", new File("image-core/src/test/resources/dct.png"))

//        def dctIntArray = ImageDataConverter.bufferedImageToIntArray(dctImage)
//        def orgImgArray = ImgMod34a.processImg(dctIntArray, image.getWidth(), image.getHeight())
//        def orgImage = ImageDataConverter.intArrayToBufferedImage(orgImgArray)
//        ImageIO.write(orgImage, "PNG", new File("image-core/src/test/resources/stage2.png"))
    }
}
