package lotto.service;

import lotto.domain.entity.Round;
import lotto.domain.entity.RoundResult;

public interface RoundApplicationService {

    int getLatestRound();

    Round startNewRound();

    Round getRoundInfo(int roundNumber);

    RoundResult closeRoundAndStartNextRound();

}
