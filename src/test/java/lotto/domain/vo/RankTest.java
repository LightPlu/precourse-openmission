package lotto.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RankTest {

    @Test
    @DisplayName("번호 6개 일치 → FIRST")
    void matchFirst() {
        Rank rank = Rank.valueOf(6, false);
        assertThat(rank).isEqualTo(Rank.FIRST);
    }

    @Test
    @DisplayName("5개 일치 + 보너스 일치 → SECOND")
    void matchSecond() {
        Rank rank = Rank.valueOf(5, true);
        assertThat(rank).isEqualTo(Rank.SECOND);
    }

    @Test
    @DisplayName("5개 일치 + 보너스 불일치 → THIRD")
    void matchThird() {
        Rank rank = Rank.valueOf(5, false);
        assertThat(rank).isEqualTo(Rank.THIRD);
    }

    @Test
    @DisplayName("번호 4개 일치 → FOURTH")
    void matchFourth() {
        Rank rank = Rank.valueOf(4, false);
        assertThat(rank).isEqualTo(Rank.FOURTH);
    }

    @Test
    @DisplayName("번호 3개 일치 → FIFTH")
    void matchFifth() {
        Rank rank = Rank.valueOf(3, false);
        assertThat(rank).isEqualTo(Rank.FIFTH);
    }

    @Test
    @DisplayName("번호가 3개 미만이면 MISS")
    void matchMiss() {
        Rank rank1 = Rank.valueOf(2, false);
        Rank rank2 = Rank.valueOf(1, true);
        Rank rank3 = Rank.valueOf(0, false);

        assertThat(rank1).isEqualTo(Rank.MISS);
        assertThat(rank2).isEqualTo(Rank.MISS);
        assertThat(rank3).isEqualTo(Rank.MISS);
    }

    @Test
    @DisplayName("각 Rank가 올바른 prize 값을 반환해야 한다")
    void prizeCheck() {
        assertThat(Rank.FIRST.getPrize()).isEqualTo(2_000_000_000);
        assertThat(Rank.SECOND.getPrize()).isEqualTo(30_000_000);
        assertThat(Rank.THIRD.getPrize()).isEqualTo(1_500_000);
        assertThat(Rank.FOURTH.getPrize()).isEqualTo(50_000);
        assertThat(Rank.FIFTH.getPrize()).isEqualTo(5_000);
        assertThat(Rank.MISS.getPrize()).isEqualTo(0);
    }
}
