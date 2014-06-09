package pl.info.rkluszczynski.image.compare.phash

import spock.lang.Specification

import javax.imageio.ImageIO

/**
 * Created by Rafal on 2014-06-09.
 */
class ImagePHashTest extends Specification {
    ImagePHash01 imagePHash01;
    ImagePHash02 imagePHash02;
    ImagePHash03 imagePHash03;

    void setup() {
        imagePHash01 = new ImagePHash01()
        imagePHash02 = new ImagePHash02()
        imagePHash03 = new ImagePHash03()
    }

    def "should calculate phash and hamming distance"() {
        when:
        println System.getProperty("user.dir")

        def img1 = ImageIO.read(new File("src/test/resources/happy-face-scaled.png"))
        def img2 = ImageIO.read(new File("src/test/resources/happy-face-rotated.png"))

        def pHash01_1 = imagePHash01.getHash(img1)
        def pHash01_2 = imagePHash01.getHash(img2)

        def pHash02_1 = imagePHash02.getHash(img1)
        def pHash02_2 = imagePHash02.getHash(img2)

        def pHash03_1 = imagePHash03.getHash(img1)
        def pHash03_2 = imagePHash03.getHash(img2)

        then:
        "0110000011000000000101000011000111000011110100000" == pHash01_1
        "0000000011000000000101000011000111100011110110000" == pHash02_1
        "0110000011000010001101000011000111100011110110000" == pHash03_1

        "0000100110000100111100000110110100000111100100001" == pHash01_2
        "0000000010000100110100000110010100000111100100001" == pHash02_2
        "0100100110000100111100000110110100000111100100001" == pHash03_2

        HammingDistance.calculate(pHash01_1, pHash01_2) == 19
        HammingDistance.calculate(pHash02_1, pHash02_2) == 15
        HammingDistance.calculate(pHash03_1, pHash03_2) == 20


        HammingDistance.calculate(pHash01_1, pHash02_1) == 4
        HammingDistance.calculate(pHash01_2, pHash02_2) == 4

        HammingDistance.calculate(pHash01_1, pHash03_1) == 4
        HammingDistance.calculate(pHash01_2, pHash03_2) == 1

        HammingDistance.calculate(pHash02_1, pHash03_1) == 4
        HammingDistance.calculate(pHash02_2, pHash03_2) == 5
    }
}
