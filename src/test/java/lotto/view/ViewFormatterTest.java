package lotto.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ViewFormatterTest {

    @ParameterizedTest
    @CsvSource(value = {
            "THREE_DIGITS_MATCH: 1: 3개 일치 (5,000원) - 1개",
            "FOUR_DIGITS_MATCH: 2: 4개 일치 (50,000원) - 2개",
            "FIVE_DIGITS_MATCH: 3: 5개 일치 (1,500,000원) - 3개",
            "FIVE_DIGITS_ONE_BONUS_MATCH: 4: 5개 일치, 보너스 볼 일치 (30,000,000원) - 4개",
            "SIX_DIGITS_MATCH: 5: 6개 일치 (2,000,000,000원) - 5개"
    }, delimiter = ':')
    void testLottoResultFormat(DigitsMatch message, int value, String expected) {
        // when
        String result = ViewFormatter.lottoResultFormat(message, value);

        // then
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "EARNING_RATE, 0.545, 총 수익률은 54.5%입니다."
    })
    void testEarningRateFormat(DigitsMatch message, double value, String expected) {
        String result = ViewFormatter.earningRateFormat(message, value);
        assertEquals(expected, result);
    }
}
