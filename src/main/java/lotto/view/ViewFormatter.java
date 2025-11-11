package lotto.view;

import static lotto.domain.NumberConstants.PERCENTAGE;

public class ViewFormatter {

    public static String lottoResultFormat(DigitsMatch message, int value) {
        return String.format(message.getMessage(), value);
    }

    public static String lottoCountFormat(String message, int value) {
        return String.format(message, value);
    }

    public static String earningRateFormat(DigitsMatch message, double value) {
        return String.format(message.getMessage(), value * PERCENTAGE.getValue());
    }

}
