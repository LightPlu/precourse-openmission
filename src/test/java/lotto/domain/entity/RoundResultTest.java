package lotto.domain.entity;

import java.util.LinkedHashMap;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RoundResultTest {

    private Map<Rank, Integer> sampleRankResults() {
        Map<Rank, Integer> map = new EnumMap<>(Rank.class);
        map.put(Rank.FIRST, 1);
        map.put(Rank.THIRD, 3);
        map.put(Rank.SECOND, 2);
        map.put(Rank.MISS, 10);
        map.put(Rank.FOURTH, 4);
        map.put(Rank.FIFTH, 5);

        return map;
    }

    @Test
    @DisplayName("RoundResult는 roundId와 rankResults로 정상 생성된다")
    void createRoundResult() {
        Map<Rank, Integer> rankResults = sampleRankResults();

        RoundResult rr = RoundResult.of(0, 5, rankResults);

        assertThat(rr.getRoundId()).isEqualTo(5);
        assertThat(rr.getRankResults()).containsAllEntriesOf(rankResults);
    }

    @Test
    @DisplayName("toMap()은 Rank 순서대로 FIRST → SECOND → THIRD → FOURTH → FIFTH → MISS를 반환한다")
    void toMapOrderTest() {
        Map<Rank, Integer> rankResults = sampleRankResults();

        RoundResult rr = RoundResult.of(0, 5, rankResults);

        Map<String, Integer> result = rr.toMap();

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("FIRST", 1);
        expected.put("SECOND", 2);
        expected.put("THIRD", 3);
        expected.put("FOURTH", 4);
        expected.put("FIFTH", 5);
        expected.put("MISS", 10);

        assertThat(result).containsExactlyEntriesOf(expected);
    }

    @Test
    @DisplayName("calculateWinningPrize()는 Rank별 상금을 계산하고 TOTAL 합계를 포함한다")
    void calculateWinningPrizeTest() {

        Map<Rank, Integer> rankResults = sampleRankResults();

        RoundResult rr = RoundResult.of(0, 5, rankResults);

        Map<String, Long> prizeMap = rr.calculateWinningPrize();

        long expectedFirst = 2_000_000_000L;
        long expectedSecond = 60_000_000L;
        long expectedThird = 4_500_000L;
        long expectedFourth = 200_000L;
        long expectedFifth = 25_000L;
        long expectedMiss = 0L;
        long expectedTotal = expectedFirst + expectedSecond + expectedThird +
                expectedFourth + expectedFifth + expectedMiss;

        assertThat(prizeMap.get("FIRST")).isEqualTo(expectedFirst);
        assertThat(prizeMap.get("SECOND")).isEqualTo(expectedSecond);
        assertThat(prizeMap.get("THIRD")).isEqualTo(expectedThird);
        assertThat(prizeMap.get("FOURTH")).isEqualTo(expectedFourth);
        assertThat(prizeMap.get("FIFTH")).isEqualTo(expectedFifth);
        assertThat(prizeMap.get("MISS")).isEqualTo(expectedMiss);
        assertThat(prizeMap.get("TOTAL")).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("랭크 결과가 없는 Rank는 toMap에서 0으로 처리된다")
    void toMapMissingRankIsZero() {
        Map<Rank, Integer> partial = new EnumMap<>(Rank.class);
        partial.put(Rank.FIRST, 1);

        RoundResult rr = RoundResult.of(0, 1, partial);

        Map<String, Integer> map = rr.toMap();

        assertThat(map.get("FIRST")).isEqualTo(1);
        assertThat(map.get("SECOND")).isEqualTo(0);
        assertThat(map.get("THIRD")).isEqualTo(0);
        assertThat(map.get("FOURTH")).isEqualTo(0);
        assertThat(map.get("FIFTH")).isEqualTo(0);
        assertThat(map.get("MISS")).isEqualTo(0);
    }
}
