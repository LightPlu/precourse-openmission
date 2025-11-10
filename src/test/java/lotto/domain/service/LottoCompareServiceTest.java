package lotto.domain.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import lotto.domain.entity.Lotto;
import lotto.domain.vo.Cash;
import lotto.domain.vo.CountResult;
import lotto.domain.vo.WinningLottoNumbers;
import lotto.utils.RandomNumberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LottoCompareServiceTest {

    @DisplayName("로또 번호를 비교한다")
    @Test
    void compare6Match() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    LottoPurchaseService purchaseService = new LottoPurchaseService(new RandomNumberGenerator());
                    List<Lotto> lottos = purchaseService.purchase(new Cash(1000));

                    WinningLottoNumbers winningNumbers = new WinningLottoNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoCompareService compareService = new LottoCompareService();

                    CountResult result = compareService.compareNumber(lottos.getFirst(), winningNumbers);

                    assertThat(result.getNumberMatchCount()).isEqualTo(6);
                },
                List.of(1, 2, 3, 4, 5, 6)
        );
    }

    @DisplayName("보너스 번호는 로또 번호에 영향을 주지 않는다")
    @Test
    void compareWithBonusMatch() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    LottoPurchaseService purchaseService = new LottoPurchaseService(new RandomNumberGenerator());
                    List<Lotto> lottos = purchaseService.purchase(new Cash(1000));

                    WinningLottoNumbers winningNumbers = new WinningLottoNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoCompareService compareService = new LottoCompareService();

                    CountResult result = compareService.compareNumber(lottos.getFirst(), winningNumbers);

                    assertThat(result.getNumberMatchCount()).isEqualTo(4);
                },
                List.of(1, 2, 3, 4, 7, 10)
        );
    }

    @DisplayName("보너스 번호를 비교한다")
    @Test
    void compareBonusMatch() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    LottoPurchaseService purchaseService = new LottoPurchaseService(new RandomNumberGenerator());
                    List<Lotto> lottos = purchaseService.purchase(new Cash(1000));

                    WinningLottoNumbers winningNumbers = new WinningLottoNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoCompareService compareService = new LottoCompareService();

                    CountResult result = compareService.compareNumber(lottos.getFirst(), winningNumbers);

                    assertThat(result.getBonusNumberMatchCount()).isEqualTo(true);
                },
                List.of(1, 2, 3, 4, 7, 10)
        );
    }
}
