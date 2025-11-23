package lotto.domain.service;

import lotto.domain.vo.Cash;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.infrastructure.RandomNumberGenerator;
import lotto.application.lottoService.LottoPurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LottoPurchaseServiceTest {

    private LottoPurchaseService lottoPurchaseService;

    class FakeRandomNumberGenerator implements RandomNumberGenerator {
        @Override
        public List<Integer> createNumbers(int min, int max, int count) {
            return List.of(1, 2, 3, 4, 5, 6);
        }
    }

    @BeforeEach
    void setUp() {
        // Fake → DomainGenerator → PurchaseService 구성
        RandomNumberGenerator fake = new FakeRandomNumberGenerator();
        LottoNumberGenerator domainGenerator = new LottoNumberGenerator(fake);

        lottoPurchaseService = new LottoPurchaseService(domainGenerator);
    }

    @Test
    @DisplayName("5000원이면 5게임가 들어있는 티켓 1개가 생성된다")
    void purchaseOneTicket() {
        Cash cash = Cash.of(5000);

        List<List<Lotto>> result = lottoPurchaseService.purchase(cash);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).hasSize(5);
    }

    @Test
    @DisplayName("6000원이면 5게임 티켓 1개 + 1게임 티켓 1개가 생성된다")
    void purchaseMixedTickets() {
        Cash cash = Cash.of(6000);

        List<List<Lotto>> result = lottoPurchaseService.purchase(cash);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).hasSize(5);
        assertThat(result.get(1)).hasSize(1);
    }

}
