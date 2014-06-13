package pl.info.rkluszczynski.image.compare

import spock.lang.Specification

/**
 * Created by Rafal on 2014-06-13.
 */
class SampleCorrelationTest extends Specification {

    def "calculate sample correlation coefficients"() {
        given:
        double[] x = values1
        double[] y = values2

        expect:
        SampleCorrelation.calculate(x, y) == correlationValue

        where:
        values1                  | values2                  | correlationValue
        [43, 21, 25, 42, 57, 59] | [99, 65, 79, 75, 87, 81] | 0.5298089018901744
        [1, 2, 3, 4, 5]          | [1, 2, 3, 4, 5]          | 0.9999999999999998
        [1, 2, 3, 4, 5]          | [5, 4, 3, 2, 1]          | -0.9999999999999998
        [1, 2, 3, 4, 5]          | [2, 3.9, 6.1, 7.8, 10]   | 0.9993566981555135
        [1, 2, 3, 4, 5]          | [10, 20, 30, 40, 50]     | 1
        [1, 2, 3, 4, 5, 6, 7]    | [8, 9, 8, 9, 8, 9, 8]    | 0
        // TODO: fix it
        [1, 2, 3, 4, 5, 6, 7] | [1, 1, 1, 1, 1, 1, 1] | Double.NaN
//        [1, 1, 1, 1, 1, 1.0000001, 1] | [1, 1.001, 1, 1, 1, 1, 1] | Double.NaN
    }
}