package lotto.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.CountResult;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LottoAggregateServiceTest {

    private final LottoAggregateService lottoAggregateService = new LottoAggregateService();

    @DisplayName("매칭 결과 목록을 등수별로 집계한다")
    @Test
    void aggregateLottoResult() {
        // given
        List<CountResult> countResults = List.of(
                new CountResult(6, false),  // 1등
                new CountResult(5, true),   // 2등
                new CountResult(5, false),  // 3등
                new CountResult(5, false),  // 3등
                new CountResult(4, false),  // 4등
                new CountResult(3, false),  // 5등
                new CountResult(2, false)   // 낙첨
        );

        // when
        LottoResult result = lottoAggregateService.aggregateLottoResult(countResults);

        // then
        assertThat(result.getResults().get(Rank.FIRST)).isEqualTo(1);
        assertThat(result.getResults().get(Rank.SECOND)).isEqualTo(1);
        assertThat(result.getResults().get(Rank.THIRD)).isEqualTo(2);
        assertThat(result.getResults().get(Rank.FOURTH)).isEqualTo(1);
        assertThat(result.getResults().get(Rank.FIFTH)).isEqualTo(1);
        assertThat(result.getResults().get(Rank.MISS)).isEqualTo(1);
    }

    @DisplayName("모두 낙첨인 경우 MISS로 집계된다")
    @Test
    void aggregateAllMiss() {
        // given
        List<CountResult> countResults = List.of(
                new CountResult(0, false),
                new CountResult(1, false),
                new CountResult(2, false)
        );

        // when
        LottoResult result = lottoAggregateService.aggregateLottoResult(countResults);

        // then
        assertThat(result.getResults().get(Rank.MISS)).isEqualTo(3);
    }

    @DisplayName("특정 등수만 당첨될 때 정확히 집계한다")
    @Test
    void aggregateSpecificRank() {
        List<CountResult> countResults = List.of(
                new CountResult(4, true),
                new CountResult(4, true),
                new CountResult(4, true),
                new CountResult(4, true)
        );

        LottoResult result = lottoAggregateService.aggregateLottoResult(countResults);

        assertThat(result.getResults().get(Rank.FOURTH)).isEqualTo(4);
    }
}
