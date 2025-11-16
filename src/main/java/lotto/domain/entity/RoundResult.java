package lotto.domain.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
        return Arrays.stream(Rank.values())
                .collect(Collectors.toMap(
                        Rank::name,
                        rank -> rankResults.getOrDefault(rank, 0),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public int getRoundId() {
        return roundId;
    }

    public Map<Rank, Integer> getRankResults() {
        return rankResults;
    }

    public Map<String, Long> calculateWinningPrize() {
        Map<String, Long> prizes = new LinkedHashMap<>();
        prizes = rankResults.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        entry -> entry.getKey().getPrize() * (long) entry.getValue()
                ));
        Long totalPrizes = prizes.values().stream().reduce(0L, Long::sum);
        prizes.put("TOTAL", totalPrizes);
        return prizes;
    }

}
