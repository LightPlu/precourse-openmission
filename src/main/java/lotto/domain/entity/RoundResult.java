package lotto.domain.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import lotto.domain.vo.Rank;

public class RoundResult {

    private final long id;
    private final long roundId;
    private final Map<Rank, Integer> rankResults;

    private RoundResult(long id ,long roundId, Map<Rank, Integer> rankResults) {
        this.id = id;
        this.roundId = roundId;
        this.rankResults = Collections.unmodifiableMap(new EnumMap<>(rankResults));
    }

    public static RoundResult of(long id, long roundId, Map<Rank, Integer> rankResults) {
        return new RoundResult(id, roundId, rankResults);
    }

    public long getId() {
        return id;
    }

    public long getRoundId() {
        return roundId;
    }

    public long getRoundNumber() {
        return roundId;
    }

    public Map<Rank, Integer> getRankResults() {
        return rankResults;
    }


}
