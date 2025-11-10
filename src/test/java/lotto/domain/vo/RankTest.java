package lotto.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RankTest {

    @DisplayName("일치하는 번호 개수와 보너스 일치 여부에 따라 올바른 등수를 반환한다")
    @ParameterizedTest
    @CsvSource({
            "6, false, FIRST",
            "5, true, SECOND",
            "5, false, THIRD",
            "4, false, FOURTH",
            "3, false, FIFTH",
            "3, true, FIFTH",
            "2, true, MISS",
            "2, false, MISS",
            "1, false, MISS",
            "0, false, MISS"
    })
    void rankValueOf(int matchCount, boolean bonusMatch, Rank expectedRank) {
        Rank rank = Rank.valueOf(matchCount, bonusMatch);

        assertThat(rank).isEqualTo(expectedRank);
    }

    @DisplayName("1등의 상금은 2,000,000,000원이다")
    @Test
    void firstPrize() {
        int prize = Rank.FIRST.getPrize();

        assertThat(prize).isEqualTo(2_000_000_000);
    }

    @DisplayName("2등의 상금은 30,000,000원이다")
    @Test
    void secondPrize() {
        int prize = Rank.SECOND.getPrize();

        assertThat(prize).isEqualTo(30_000_000);
    }

    @DisplayName("3등의 상금은 1,500,000원이다")
    @Test
    void thirdPrize() {
        int prize = Rank.THIRD.getPrize();

        assertThat(prize).isEqualTo(1_500_000);
    }

    @DisplayName("4등의 상금은 50,000원이다")
    @Test
    void fourthPrize() {
        int prize = Rank.FOURTH.getPrize();

        assertThat(prize).isEqualTo(50_000);
    }

    @DisplayName("5등의 상금은 5,000원이다")
    @Test
    void fifthPrize() {
        int prize = Rank.FIFTH.getPrize();

        assertThat(prize).isEqualTo(5_000);
    }

    @DisplayName("낙첨의 상금은 0원이다")
    @Test
    void missPrize() {
        int prize = Rank.MISS.getPrize();

        assertThat(prize).isEqualTo(0);
    }
}

