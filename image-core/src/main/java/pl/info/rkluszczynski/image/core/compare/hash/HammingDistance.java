package pl.info.rkluszczynski.image.core.compare.hash;

/**
 * http://blogs.ucl.ac.uk/chime/2010/06/28/java-example-code-of-common-similarity-algorithms-used-in-data-mining/
 */
final
public class HammingDistance {
    public static final String NOT_EQUAL_SEQUENCES_LENGTH = "Sequences are not equal for calculating Hamming distance";

    private HammingDistance() {
    }

    public static int calculate(String sequence1, String sequence2) {
        char[] s1 = sequence1.toCharArray();
        char[] s2 = sequence2.toCharArray();

        if (s1.length != s2.length) {
            throw new IllegalArgumentException(NOT_EQUAL_SEQUENCES_LENGTH);
        }

        int result = 0;
        for (int i = 0; i < s1.length; ++i) {
            if (s1[i] != s2[i]) ++result;
        }
        return result;
    }
}
