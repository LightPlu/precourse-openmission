package lotto.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lotto.domain.entity.Lotto;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.Cash;
import lotto.domain.vo.Rank;
import lotto.infrastructure.InMemoryLottoRepository;
import lotto.domain.repository.LottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LottoServiceImplTest {

    private LottoServiceImpl lottoService;
    private LottoRepository lottoRepository;

    @BeforeEach
    void setUp() {
        lottoRepository = new InMemoryLottoRepository();
        lottoService = new LottoServiceImpl();
    }

    @DisplayName("로또를 구매하면 Repository에 저장된다")
    @Test
    void buyLottoAndSave() {
        List<Lotto> expectedLottos = new ArrayList<>();
        expectedLottos.add(new Lotto(List.of(1, 2, 3, 4, 5, 6)));
        expectedLottos.add(new Lotto(List.of(7, 8, 9, 10, 11, 12)));

        lottoRepository.saveAll(expectedLottos);

        assertThat(lottoRepository.findAll()).isEqualTo(expectedLottos);
    }

    @DisplayName("로또를 구매하고 개수를 반환한다")
    @Test
    void buyLottoReturnsCount() {
        int money = 5000;
        int count = lottoService.buyLottoAndSave(money);

        assertThat(count).isEqualTo(5);
    }

    @DisplayName("구매한 로또가 Repository에 저장되고 조회할 수 있다")
    @Test
    void savedLottosCanBeRetrieved() {
        int money = 3000;
        lottoService.buyLottoAndSave(money);

        List<Lotto> savedLottos = lottoService.getSavedLottos();

        assertThat(savedLottos).hasSize(3);
        assertThat(savedLottos.getFirst().getNumbers()).hasSize(6);
    }

    @DisplayName("각 등수별 당첨 시 수익률을 계산한다")
    @ParameterizedTest
    @CsvSource({
            "FIFTH, 1, 0.625",
            "FOURTH, 1, 6.25",
            "MISS, 8, 0",
            "FIRST, 1, 250000"
    })
    void calculateEarningRateForEachRank(Rank rank, Integer count, double expectedEarningRate) {
        Cash cash = new Cash(8000);
        Map<Rank, Integer> results = new EnumMap<>(Rank.class);
        results.put(rank, count);
        LottoResult lottoResult = new LottoResult(results);

        double earningRate = lottoService.totalWinningsMoney(cash, lottoResult);

        assertThat(earningRate).isEqualTo(expectedEarningRate);
    }

    @DisplayName("여러 등수 혼합 당첨 시 수익률을 계산한다")
    @Test
    void calculateEarningRateForMixedRanks() {
        Cash cash = new Cash(10000);
        Map<Rank, Integer> results = new EnumMap<>(Rank.class);
        results.put(Rank.FIFTH, 2);
        results.put(Rank.FOURTH, 1);
        results.put(Rank.MISS, 7);
        LottoResult lottoResult = new LottoResult(results);

        double earningRate = lottoService.totalWinningsMoney(cash, lottoResult);

        assertThat(earningRate).isEqualTo(6.0);
    }

    @DisplayName("저장된 로또와 당첨 번호를 비교하여 5등 1개를 확인한다")
    @Test
    void compareLottoWithFifthRank() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // 3개 로또 구매 (고정된 번호)
                    lottoService.buyLottoAndSave(3000);

                    // 당첨 번호: 1,2,3,4,5,6 (첫 번째 로또와 3개 일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    // 결과 검증
                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.FIFTH)).isEqualTo(1);  // 5등 1개
                    assertThat(results.getOrDefault(Rank.MISS, 0)).isEqualTo(2);  // 나머지 낙첨
                },
                List.of(1, 2, 3, 10, 11, 12),  // 첫 번째 로또: 3개 일치
                List.of(13, 14, 15, 16, 17, 18),  // 두 번째 로또: 낙첨
                List.of(19, 20, 21, 22, 23, 24)   // 세 번째 로또: 낙첨
        );
    }

    @DisplayName("저장된 로또와 당첨 번호를 비교하여 4등 1개를 확인한다")
    @Test
    void compareLottoWithFourthRank() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(2000);

                    // 당첨 번호: 1,2,3,4,5,6 (첫 번째 로또와 4개 일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.FOURTH)).isEqualTo(1);  // 4등 1개
                    assertThat(results.getOrDefault(Rank.MISS, 0)).isEqualTo(1);  // 나머지 낙첨
                },
                List.of(1, 2, 3, 4, 10, 11),  // 첫 번째 로또: 4개 일치
                List.of(13, 14, 15, 16, 17, 18)   // 두 번째 로또: 낙첨
        );
    }

    @DisplayName("저장된 로또와 당첨 번호를 비교하여 3등 1개를 확인한다")
    @Test
    void compareLottoWithThirdRank() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(1000);

                    // 당첨 번호: 1,2,3,4,5,6, 보너스: 10 (5개 일치, 보너스 불일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 10);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.THIRD)).isEqualTo(1);  // 3등 1개
                },
                List.of(1, 2, 3, 4, 5, 11)  // 5개 일치, 보너스(10) 불일치
        );
    }

    @DisplayName("저장된 로또와 당첨 번호를 비교하여 2등 1개를 확인한다")
    @Test
    void compareLottoWithSecondRank() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(1000);

                    // 당첨 번호: 1,2,3,4,5,6, 보너스: 7 (5개 일치, 보너스 일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.SECOND)).isEqualTo(1);  // 2등 1개
                },
                List.of(1, 2, 3, 4, 5, 7)  // 5개 일치, 보너스(7) 일치
        );
    }

    @DisplayName("저장된 로또와 당첨 번호를 비교하여 1등 1개를 확인한다")
    @Test
    void compareLottoWithFirstRank() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(1000);

                    // 당첨 번호: 1,2,3,4,5,6 (6개 모두 일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.FIRST)).isEqualTo(1);  // 1등 1개
                },
                List.of(1, 2, 3, 4, 5, 6)  // 6개 모두 일치
        );
    }

    @DisplayName("저장된 로또 전부 낙첨인 경우를 확인한다")
    @Test
    void compareLottoWithAllMiss() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(3000);

                    // 당첨 번호: 1,2,3,4,5,6 (모든 로또가 2개 이하 일치)
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.MISS)).isEqualTo(3);  // 전부 낙첨
                },
                List.of(1, 2, 10, 11, 12, 13),  // 2개 일치 (낙첨)
                List.of(14, 15, 16, 17, 18, 19),  // 0개 일치 (낙첨)
                List.of(3, 20, 21, 22, 23, 24)   // 1개 일치 (낙첨)
        );
    }

    @DisplayName("저장된 로또 중 여러 등수가 혼합된 경우를 확인한다")
    @Test
    void compareLottoWithMixedRanks() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.buyLottoAndSave(5000);

                    // 당첨 번호: 1,2,3,4,5,6, 보너스: 7
                    LottoResult result = lottoService.compareLotto(List.of(1, 2, 3, 4, 5, 6), 7);

                    Map<Rank, Integer> results = result.getResults();
                    assertThat(results.get(Rank.FIRST)).isEqualTo(1);   // 1등 1개
                    assertThat(results.get(Rank.SECOND)).isEqualTo(1);  // 2등 1개
                    assertThat(results.get(Rank.FIFTH)).isEqualTo(1);   // 5등 1개
                    assertThat(results.get(Rank.MISS)).isEqualTo(2);    // 낙첨 2개
                },
                List.of(1, 2, 3, 4, 5, 6),      // 1등 (6개 일치)
                List.of(1, 2, 3, 4, 5, 7),      // 2등 (5개 일치, 보너스 일치)
                List.of(1, 2, 3, 10, 11, 12),   // 5등 (3개 일치)
                List.of(1, 2, 13, 14, 15, 16),  // 낙첨 (2개 일치)
                List.of(20, 21, 22, 23, 24, 25) // 낙첨 (0개 일치)
        );
    }
}

