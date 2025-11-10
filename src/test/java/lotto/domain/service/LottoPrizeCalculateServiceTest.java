package lotto.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.Cash;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LottoPrizeCalculateServiceTest {

    @DisplayName("수익률을 집계한다")
    @Test
    void calculateEarningRateTest() {
        LottoPrizeCalculateService service = new LottoPrizeCalculateService();
        Cash cash = new Cash(2000);
        Map<Rank, Integer> results = new HashMap<>();
        results.put(Rank.SECOND, 1);
        results.put(Rank.FOURTH, 2);
        results.put(Rank.FIFTH, 3);
        LottoResult lottoResult = new LottoResult(results);

        double calculateResult = service.calculateEarningRate(cash, lottoResult);
        double expectedResult = (double) (50000 * 2 + 5000 * 3 + 30000000) / cash.getMoney();

        assertThat(calculateResult).isEqualTo(expectedResult);

    }
}
