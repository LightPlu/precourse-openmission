package lotto.domain.entity;

import java.util.List;
import java.util.Map;
import lotto.domain.vo.Rank;

public class LottoResult {
    private final Map<Rank, Integer> results;

    public LottoResult(Map<Rank, Integer> results) {
        this.results = results;
    }

    public Map<Rank, Integer> getResults() {
        return results;
    }

    public List<Integer> getResultValuesOrdered() {
        return List.of(
                results.getOrDefault(Rank.FIFTH, 0),    // 5등 (3개 일치)
                results.getOrDefault(Rank.FOURTH, 0),   // 4등 (4개 일치)
                results.getOrDefault(Rank.THIRD, 0),    // 3등 (5개 일치)
                results.getOrDefault(Rank.SECOND, 0),   // 2등 (5개+보너스)
                results.getOrDefault(Rank.FIRST, 0)     // 1등 (6개 일치)
        );
    }
}
