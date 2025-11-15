package lotto.service;

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
                .orElseThrow(() -> new RuntimeException("해당 회차가 아직 끝나지 않았습니다."));
        return result.toMap();
    }
}
