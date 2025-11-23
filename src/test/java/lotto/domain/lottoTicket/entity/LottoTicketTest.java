package lotto.domain.lottoTicket.entity;

import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LottoTicketTest {

    private Lotto lotto(Integer... nums) {
        return Lotto.of(
                List.of(nums).stream()
                        .map(LottoNumber::of)
                        .toList()
        );
    }

    @Test
    @DisplayName("로또 티켓은 최소 1게임, 최대 5게임까지만 포함할 수 있다")
    void lottoTicketRange() {
        // 0개 → 예외
        assertThatThrownBy(() ->
                LottoTicket.of(1, List.of())
        ).isInstanceOf(IllegalArgumentException.class);

        // 6개 → 예외
        List<Lotto> sixGames = List.of(
                lotto(1, 2, 3, 4, 5, 6),
                lotto(7, 8, 9, 10, 11, 12),
                lotto(13, 14, 15, 16, 17, 18),
                lotto(19, 20, 21, 22, 23, 24),
                lotto(25, 26, 27, 28, 29, 30),
                lotto(31, 32, 33, 34, 35, 36)
        );

        assertThatThrownBy(() ->
                LottoTicket.of(1, sixGames)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정상 범위(1~5게임)면 LottoTicket 생성 성공")
    void validLottoTicket() {
        List<Lotto> games = List.of(
                lotto(1, 2, 3, 4, 5, 6),
                lotto(10, 11, 12, 13, 14, 15)
        );

        LottoTicket ticket = LottoTicket.of(3, games);

        assertThat(ticket.getLottos()).hasSize(2);
        assertThat(ticket.getRoundId()).isEqualTo(3);
    }

    @Test
    @DisplayName("numbersAsCsv()는 로또 리스트를 CSV 문자열로 변환한다")
    void numbersAsCsvTest() {
        Lotto lotto1 = lotto(1, 2, 3, 4, 5, 6);
        Lotto lotto2 = lotto(10, 11, 12, 13, 14, 15);

        LottoTicket ticket = LottoTicket.of(1, List.of(lotto1, lotto2));

        String csv = ticket.numbersAsCsv();

        assertThat(csv).isEqualTo(
                "1,2,3,4,5,6;10,11,12,13,14,15"
        );
    }

    @Test
    @DisplayName("calculateLottoSize()는 티켓 내 로또 개수를 반환한다")
    void calculateSize() {
        Lotto lotto1 = lotto(1, 2, 3, 4, 5, 6);
        Lotto lotto2 = lotto(10, 11, 12, 13, 14, 15);
        Lotto lotto3 = lotto(20, 21, 22, 23, 24, 25);

        LottoTicket ticket = LottoTicket.of(5, List.of(lotto1, lotto2, lotto3));

        assertThat(ticket.calculateLottoSize()).isEqualTo(3);
    }
}
