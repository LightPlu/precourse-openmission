package lotto.domain.vo;

import java.util.List;

public class LottoResult {
    private final List<PrizeDetail> results;

    public LottoResult(List<PrizeDetail> results) {
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
