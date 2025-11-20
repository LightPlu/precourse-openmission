package lotto.presentation.view.utils;

import static lotto.presentation.controller.ApplicationErrorMessage.INPUT_VALUE_IS_INVALID;

import java.util.Arrays;
import java.util.List;

public class TypeConverter {

    public int convertInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_VALUE_IS_INVALID.getMessage());
        }
    }

    public List<Integer> winningNumberTypeConvert(String input) {
        try {
            return Arrays.stream(input.split(","))
                    .map(Integer::parseInt)
                    .toList();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_VALUE_IS_INVALID.getMessage());
        }
    }
}
