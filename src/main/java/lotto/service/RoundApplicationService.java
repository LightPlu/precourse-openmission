package lotto.service;

import java.util.List;
import lotto.domain.entity.Round;

public interface RoundApplicationService {

    int getLatestRound();

    Round startNewRound();

    Round getRoundInfo(int roundNumber);

    void closeRoundAndStartNextRound();

    List<Integer> findAllRoundNumbers();

}
