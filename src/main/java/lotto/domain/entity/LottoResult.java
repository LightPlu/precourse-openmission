package lotto.domain.entity;

import java.util.List;
import lotto.domain.vo.PrizeDetail;

public class LottoResult {
    private final int round;
    private final List<PrizeDetail> results;

    public LottoResult(int round, List<PrizeDetail> results) {
        this.round = round;
        this.results = List.copyOf(results);
    }

    public List<PrizeDetail> getResults() {
        return results;
    }

    public double calculateAllPrizes() {
        return results.stream()
                .mapToDouble(PrizeDetail::getTotalPrize)
                .sum();
    }

}
