package lotto.application.roundService;

import java.util.List;
import lotto.domain.round.entity.Round;

public interface RoundApplicationService {

    int getLatestRound();

    Round startNewRound();

    void closeRoundAndStartNextRound();

    List<Integer> findAllRoundNumbers();

    int findRoundIdByRoundNumber(int roundNumber);

}
