package lotto.domain.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import lotto.domain.vo.Rank;

public class RoundResult {

    private final int id;
    private final int roundId;
    private final Map<Rank, Integer> rankResults;

    private RoundResult(int id ,int roundId, Map<Rank, Integer> rankResults) {
        this.id = id;
        this.roundId = roundId;
        this.rankResults = Collections.unmodifiableMap(new EnumMap<>(rankResults));
    }

    public static RoundResult of(int id, int roundId, Map<Rank, Integer> rankResults) {
        return new RoundResult(id, roundId, rankResults);
    }

    public Map<String, Integer> toMap() {
        return rankResults.entrySet().stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                entry -> entry.getKey().name(),   // "FIRST", "SECOND"
                                Map.Entry::getValue
                        )
                );
    }

    public int getRoundId() {
        return roundId;
    }

    public Map<Rank, Integer> getRankResults() {
        return rankResults;
    }


}
