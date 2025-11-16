package lotto.service;

import static lotto.service.ServiceErrorMessage.NOT_FINISHED_ROUND;

import java.util.Map;
import lotto.domain.entity.RoundResult;
import lotto.domain.repository.RoundRepository;

public class RoundResultApplicationServiceImpl implements RoundResultApplicationService {

    private RoundRepository roundRepository;

    public RoundResultApplicationServiceImpl(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Override
    public Map<String, Integer> getRoundResult(int roundNumber) {
        RoundResult result = roundRepository.findRoundResultByRoundId(roundNumber)
                .orElseThrow(() -> new RuntimeException(NOT_FINISHED_ROUND.getMessage()));
        return result.toMap();
    }
}
