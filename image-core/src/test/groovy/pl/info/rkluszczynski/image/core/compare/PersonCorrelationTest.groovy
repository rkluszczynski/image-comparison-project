package pl.info.rkluszczynski.image.core.compare

import spock.lang.Specification

/**
 * Created by Rafal on 2014-06-13.
 */
class PersonCorrelationTest extends Specification {

    def "calculate Pearson correlation coefficient"() {
        given:
        double[] x = values1
        double[] y = values2

        expect:
        PersonCorrelation.calculate(x, y) == correlationValue

        where:
        values1                  | values2                  | correlationValue
        [43, 21, 25, 42, 57, 59] | [99, 65, 79, 75, 87, 81] | 0.5298089018901744
        [1, 2, 3, 4, 5]          | [1, 2, 3, 4, 5]          | 1
        [1, 2, 3, 4, 5]          | [5, 4, 3, 2, 1]          | -1
        [1, 2, 3, 4, 5]          | [2, 3.9, 6.1, 7.8, 10]   | 0.9993566981555138
        [1, 2, 3, 4, 5]          | [10, 20, 30, 40, 50]     | 1
        [1, 2, 3, 4, 5, 6, 7]    | [8, 9, 8, 9, 8, 9, 8]    | 0
        // TODO: fix it
//        [1, 2, 3, 4, 5, 6, 7]    | [1, 1, 1, 1, 1, 1, 1]    | Double.NaN
//        [1, 1, 1, 1, 1, 1.0000001, 1]    | [1, 1.001, 1, 1, 1, 1, 1]    | Double.NaN
    }

    def "calculate Pearson correlation coefficient 2"() {
        given:
        double[] x = values1
        double[] y = values2

        expect:
        PersonCorrelation.calculate2(x, y) == correlationValue

        where:
        values1                  | values2                  | correlationValue
        [43, 21, 25, 42, 57, 59] | [99, 65, 79, 75, 87, 81] | 0.5298089018901745
        [1, 2, 3, 4, 5]          | [1, 2, 3, 4, 5]          | 0.9999999999999998
        [1, 2, 3, 4, 5]          | [5, 4, 3, 2, 1]          | -0.9999999999999998
        [1, 2, 3, 4, 5]          | [2, 3.9, 6.1, 7.8, 10]   | 0.9993566981555132
        [1, 2, 3, 4, 5]          | [10, 20, 30, 40, 50]     | 0.9999999999999998
        [1, 2, 3, 4, 5, 6, 7]    | [8, 9, 8, 9, 8, 9, 8]    | -2.5639502485114194E-16

        // TODO: fix it
        [1, 2, 3, 4, 5, 6, 7] | [8, 8, 8, 8, 8, 8, 8] | Double.NaN
    }
}
