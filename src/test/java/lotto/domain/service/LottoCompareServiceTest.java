package lotto.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import lotto.domain.entity.LottoTicket;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.vo.Lotto;
import lotto.domain.vo.LottoNumber;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoCompareServiceTest {

    private final LottoCompareService service = new LottoCompareService();

    private LottoNumber n(int v) {
        return LottoNumber.of(v);
    }

    private Lotto lotto(List<Integer> nums) {
        return Lotto.of(
                nums.stream()
                        .map(LottoNumber::of)
                        .toList()
        );
    }

    private WinningLottoNumbers winning(int bonus, List<Integer> nums) {
        return WinningLottoNumbers.of(
                0,
                nums.stream().map(LottoNumber::of).toList(),
                LottoNumber.of(bonus),
                1
        );
    }

    @Test
    @DisplayName("6개 모두 일치하면 FIRST(1등) 으로 카운트된다")
    void matchFirstRank() {
        LottoTicket ticket = LottoTicket.of(1, List.of(
                lotto(List.of(1, 2, 3, 4, 5, 6))
        ));
        WinningLottoNumbers winning = winning(7, List.of(1, 2, 3, 4, 5, 6));

        Map<Rank, Integer> result = service.compareTicket(ticket, winning);

        assertThat(result.get(Rank.FIRST)).isEqualTo(1);
    }

    @Test
    @DisplayName("5개 + 보너스 일치하면 SECOND(2등) 카운트된다")
    void matchSecondRank() {
        LottoTicket ticket = LottoTicket.of(1, List.of(
                lotto(List.of(1, 2, 3, 4, 5, 7))
        ));
        WinningLottoNumbers winning = winning(7, List.of(1, 2, 3, 4, 5, 8));

        Map<Rank, Integer> result = service.compareTicket(ticket, winning);

        assertThat(result.get(Rank.SECOND)).isEqualTo(1);
    }

    @Test
    @DisplayName("5개 일치하면 THIRD(3등) 카운트된다")
    void matchThirdRank() {
        LottoTicket ticket = LottoTicket.of(1, List.of(
                lotto(List.of(1, 2, 3, 4, 5, 6))
        ));
        WinningLottoNumbers winning = winning(8, List.of(1, 2, 3, 4, 5, 7));

        Map<Rank, Integer> result = service.compareTicket(ticket, winning);

        assertThat(result.get(Rank.THIRD)).isEqualTo(1);
    }

    @Test
    @DisplayName("티켓 내 여러 게임이 있는 경우 랭크별로 누적 카운트가 정상 동작한다")
    void multipleGamesCount() {
        LottoTicket ticket = LottoTicket.of(1, List.of(
                lotto(List.of(1, 2, 3, 4, 5, 6)),   // FIRST
                lotto(List.of(1, 2, 3, 4, 5, 7)),   // SECOND
                lotto(List.of(1, 2, 3, 4, 5, 10)),  // THIRD
                lotto(List.of(1, 2, 3, 4, 10, 11)), // FOURTH
                lotto(List.of(1, 2, 3, 10, 11, 12))// FIFTH
        ));

        WinningLottoNumbers winning = winning(7, List.of(1, 2, 3, 4, 5, 6));

        Map<Rank, Integer> result = service.compareTicket(ticket, winning);

        assertThat(result.get(Rank.FIRST)).isEqualTo(1);
        assertThat(result.get(Rank.SECOND)).isEqualTo(1);
        assertThat(result.get(Rank.THIRD)).isEqualTo(1);
        assertThat(result.get(Rank.FOURTH)).isEqualTo(1);
        assertThat(result.get(Rank.FIFTH)).isEqualTo(1);
        assertThat(result.get(Rank.MISS)).isEqualTo(0);
    }

}
