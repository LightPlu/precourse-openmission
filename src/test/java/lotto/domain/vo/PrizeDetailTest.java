package lotto.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrizeDetailTest {

    @Test
    @DisplayName("PrizeDetail.of()는 rank 상금 * count 를 계산해야 한다")
    void calculateTotalPrize() {
        // given
        Rank rank = Rank.FOURTH; // 예: 50,000원
        int count = 3;

        // when
        PrizeDetail detail = PrizeDetail.of(rank, count);

        // then
        assertThat(detail.getRank()).isEqualTo(rank);
        assertThat(detail.getCount()).isEqualTo(count);

        long expectedTotal = (long) rank.getPrize() * count;
        assertThat(detail.getTotalPrize()).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("count가 0일 때 totalPrize는 0이어야 한다")
    void totalPrizeZero() {
        PrizeDetail detail = PrizeDetail.of(Rank.THIRD, 0);

        assertThat(detail.getTotalPrize()).isEqualTo(0);
    }

    @Test
    @DisplayName("동일 Rank와 count면 equals() 결과가 true이다")
    void equalsTest() {
        PrizeDetail d1 = PrizeDetail.of(Rank.FIFTH, 2);
        PrizeDetail d2 = PrizeDetail.of(Rank.FIFTH, 2);

        assertThat(d1).isEqualTo(d2);
        assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
    }

    @Test
    @DisplayName("Rank 또는 count가 다르면 equals() 결과는 false이다")
    void notEqualsTest() {
        PrizeDetail d1 = PrizeDetail.of(Rank.SECOND, 1);
        PrizeDetail d2 = PrizeDetail.of(Rank.SECOND, 2);
        PrizeDetail d3 = PrizeDetail.of(Rank.THIRD, 1);

        assertThat(d1).isNotEqualTo(d2);
        assertThat(d1).isNotEqualTo(d3);
    }
}
