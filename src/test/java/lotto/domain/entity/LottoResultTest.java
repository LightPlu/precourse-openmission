package lotto.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoResultTest {

    @DisplayName("로또 결과를 생성하고 조회한다")
    @Test
    void createLottoResult() {
        Map<Rank, Integer> results = new HashMap<>();
        results.put(Rank.FIRST, 1);
        results.put(Rank.SECOND, 0);
        results.put(Rank.THIRD, 2);
        results.put(Rank.FOURTH, 3);
        results.put(Rank.FIFTH, 5);

        LottoResult lottoResult = new LottoResult(results);

        assertThat(lottoResult.getResults()).isEqualTo(results);
        assertThat(lottoResult.getResults().get(Rank.FIRST)).isEqualTo(1);
        assertThat(lottoResult.getResults().get(Rank.THIRD)).isEqualTo(2);
        assertThat(lottoResult.getResults().get(Rank.FOURTH)).isEqualTo(3);
        assertThat(lottoResult.getResults().get(Rank.FIFTH)).isEqualTo(5);
    }

    @DisplayName("빈 결과로 로또 결과를 생성한다")
    @Test
    void createEmptyLottoResult() {
        Map<Rank, Integer> emptyResults = new HashMap<>();

        LottoResult lottoResult = new LottoResult(emptyResults);

        assertThat(lottoResult.getResults()).isEmpty();
    }
}

