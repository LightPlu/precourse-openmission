package lotto.view;

import static lotto.view.DigitsMatch.EARNING_RATE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class UserOutputView {

    private static final String WINNING_STATICS_MESSAGE = "당첨 통계";
    private static final String BAR = "---";
    private static final String LOTTO_COUNT_MESSAGE = "%d개를 구매했습니다.";


    public void printTotalStatics(List<Integer> winningStats, double earningRate) {
        printWinningStaticsMessage();
        List<DigitsMatch> messages = Arrays.asList(DigitsMatch.values());

        IntStream.range(0, winningStats.size())
                .forEach(i ->
                        System.out.println(
                                ViewFormatter.lottoResultFormat(messages.get(i), winningStats.get(i))
                        ));

        System.out.println(ViewFormatter.earningRateFormat(EARNING_RATE, earningRate));
    }

    private void printWinningStaticsMessage() {
        System.out.println(WINNING_STATICS_MESSAGE);
        System.out.println(BAR);
    }

    public void printLottoCountMessage(int count) {
        System.out.println(ViewFormatter.lottoCountFormat(LOTTO_COUNT_MESSAGE, count));
    }

    public void printLottoHistory(String lottoHistory) {
        System.out.println(lottoHistory);
    }

}
