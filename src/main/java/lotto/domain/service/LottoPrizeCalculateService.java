package lotto.domain.service;

import java.util.Map;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.Cash;
import lotto.domain.vo.Rank;

public class LottoPrizeCalculateService {

    public double calculateEarningRate(Cash cash, LottoResult lottoResult) {
        return calculatePrize(lottoResult) / cash.getMoney();
    }

    private double calculatePrize(LottoResult lottoResult) {
        Map<Rank, Integer> results = lottoResult.getResults();

        return results.entrySet()
                .stream()
                .mapToDouble(entry ->
                        entry.getKey().getPrize() * entry.getValue())
                .sum();
    }
}
