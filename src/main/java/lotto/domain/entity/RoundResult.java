package lotto.domain.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import lotto.domain.vo.Rank;

public class RoundResult {

    private final int roundNumber;
    private final Map<Rank, Integer> rankResults;

    public RoundResult(int roundNumber, Map<Rank, Integer> rankResults) {
        this.roundNumber = roundNumber;
        this.rankResults = Collections.unmodifiableMap(new EnumMap<>(rankResults));
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Map<Rank, Integer> getRankResults() {
        return rankResults;
    }


}
