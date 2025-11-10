package lotto.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lotto.domain.entity.Lotto;
import lotto.domain.vo.Cash;
import lotto.utils.RandomNumberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoPurchaseServiceTest {

    @DisplayName("1000원으로 1개의 로또를 구매한다")
    @Test
    void purchaseOneLotto() {
        Cash cash = new Cash(1000);
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
        LottoPurchaseService lottoPurchaseService = new LottoPurchaseService(randomNumberGenerator);

        List<Lotto> lottos = lottoPurchaseService.purchase(cash);

        assertThat(lottos).hasSize(1);
        assertThat(lottos.getFirst().getNumbers()).hasSize(6);
    }

    @DisplayName("3000원으로 3개의 로또를 구매한다")
    @Test
    void purchaseThreeLotto() {
        Cash cash = new Cash(3000);
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
        LottoPurchaseService lottoPurchaseService = new LottoPurchaseService(randomNumberGenerator);

        List<Lotto> lottos = lottoPurchaseService.purchase(cash);

        assertThat(lottos).hasSize(3);
    }

}
