import java.util.ArrayList;

public class BitNumber {
    static final Integer[][] ONE = {
            {0, 0, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0}};
    static final Integer[][] TWO = {
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 1, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] THREE = {
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] FOUR = {
            {0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0}};
    static final Integer[][] FIVE = {
            {0, 1, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] SIX = {
            {0, 1, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] SEVEN = {
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 1, 0, 0},
            {0, 1, 0, 0, 0}};
    static final Integer[][] EIGHT = {
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] NINE = {
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] ZERO = {
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0}};
    static final Integer[][] TEN = {
            {0, 0, 1, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 1, 0, 1, 1, 1, 0}};

    static ArrayList<Integer[][]> convert(final int number) {
        ArrayList<Integer[][]> result = new ArrayList<>();
        char[] chars = Integer.toString(number).toCharArray();
        for (char ch : chars) {
            switch (ch) {
                case '0':
                    result.add(ZERO);
                    break;
                case '1':
                    result.add(ONE);
                    break;
                case '2':
                    result.add(TWO);
                    break;
                case '3':
                    result.add(THREE);
                    break;
                case '4':
                    result.add(FOUR);
                    break;
                case '5':
                    result.add(FIVE);
                    break;
                case '6':
                    result.add(SIX);
                    break;
                case '7':
                    result.add(SEVEN);
                    break;
                case '8':
                    result.add(EIGHT);
                    break;
                case '9':
                    result.add(NINE);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        return result;
    }
}
