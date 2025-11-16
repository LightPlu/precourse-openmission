package lotto.service;

import java.util.List;
import lotto.domain.entity.Round;

public interface RoundApplicationService {

    int getLatestRound();

    Round startNewRound();

    void closeRoundAndStartNextRound();

    List<Integer> findAllRoundNumbers();

}
