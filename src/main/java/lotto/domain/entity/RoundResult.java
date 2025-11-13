package lotto.domain.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import lotto.domain.vo.Lotto;
import lotto.domain.vo.Rank;

public class RoundResult {

    private final long roundId;
    private final Map<Rank, Integer> rankResults;

    private RoundResult(long roundId, Map<Rank, Integer> rankResults) {
        this.roundId = roundId;
        this.rankResults = Collections.unmodifiableMap(new EnumMap<>(rankResults));
    }

    public static RoundResult of(long roundId, Map<Rank, Integer> rankResults) {
        return new RoundResult(roundId, rankResults);
    }

    public long getRoundNumber() {
        return roundId;
    }

    public Map<Rank, Integer> getRankResults() {
        return rankResults;
    }


}
