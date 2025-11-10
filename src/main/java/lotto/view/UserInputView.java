package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import lotto.utils.TypeConverter;

public class UserInputView {

    TypeConverter typeConverter = new TypeConverter();

    private static final String PRICE_INPUT_MESSAGE = "구입금액을 입력해 주세요.";
    private static final String WINNING_NUMBERS_INPUT_MESSAGE = "당첨 번호를 입력해 주세요.";
    private static final String BONUS_NUMBER_INPUT_MESSAGE = "보너스 번호를 입력해 주세요.";

    public int printPriceMessage() {
        System.out.println(PRICE_INPUT_MESSAGE);
        String input = Console.readLine();
        return typeConverter.priceTypeConvert(input);
    }

    public List<Integer> printWinningNumbersMessage() {
        System.out.println(WINNING_NUMBERS_INPUT_MESSAGE);
        String input = Console.readLine();
        return typeConverter.winningNumberTypeConvert(input);
    }

    public int printBonusNumbersMessage() {
        System.out.println(BONUS_NUMBER_INPUT_MESSAGE);
        String input = Console.readLine();
        return typeConverter.bonusNumberTypeConvert(input);
    }
}
