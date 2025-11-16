package lotto.service;

import java.util.Map;

public interface RoundResultApplicationService {

    Map<String, Integer> getRoundResult(int roundNumber);

    Map<String, Long> calculateWinningPrize(int roundNumber);
}
