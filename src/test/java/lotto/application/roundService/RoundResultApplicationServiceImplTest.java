package lotto.application.roundService;

import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoundResultApplicationServiceImplTest {

    private RoundRepository roundRepository;
    private LottoTicketRepository lottoTicketRepository;
    private RoundResultApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        roundRepository = mock(RoundRepository.class);
        lottoTicketRepository = mock(LottoTicketRepository.class);
        service = new RoundResultApplicationServiceImpl(lottoTicketRepository, roundRepository);
    }

    private Map<Rank, Integer> sampleRankResults() {
        Map<Rank, Integer> map = new EnumMap<>(Rank.class);
        map.put(Rank.FIRST, 1);
        map.put(Rank.SECOND, 2);
        map.put(Rank.THIRD, 3);
        map.put(Rank.FOURTH, 4);
        map.put(Rank.FIFTH, 5);
        map.put(Rank.MISS, 10);
        return map;
    }

    private Round sampleRoundWithResult() {
        Round round = new Round(1, 1);

        WinningLottoNumbers winning = WinningLottoNumbers.of(
                1,
                List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ),
                LottoNumber.of(7),
                1
        );
        round.registerWinningNumbers(winning);

        RoundResult result = RoundResult.of(0, 1, sampleRankResults());
        round.registerRoundResult(result);

        return round;
    }


    @Test
    @DisplayName("getRoundResult() → RoundResult.toMap() 반환")
    void getRoundResult_success() {

        when(roundRepository.findByRoundId(1))
                .thenReturn(Optional.of(sampleRoundWithResult()));

        Map<String, Integer> result = service.getRoundResult(1);

        LinkedHashMap<String, Integer> expectedResult = new LinkedHashMap<>();
        expectedResult.put("FIRST", 1);
        expectedResult.put("SECOND", 2);
        expectedResult.put("THIRD", 3);
        expectedResult.put("FOURTH", 4);
        expectedResult.put("FIFTH", 5);
        expectedResult.put("MISS", 10);

        assertThat(result).containsExactlyEntriesOf(expectedResult);

        verify(roundRepository).findByRoundId(1);
    }

    @Test
    @DisplayName("getRoundResult() → 라운드 결과가 없으면 예외 발생")
    void getRoundResult_fail_notFinished() {

        when(roundRepository.findByRoundId(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRoundResult(1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("calculateWinningPrize() → 당첨 금액 계산 성공")
    void calculateWinningPrize_success() {
        when(roundRepository.findByRoundId(1))
                .thenReturn(Optional.of(sampleRoundWithResult()));

        Map<String, Long> result = service.calculateWinningPrize(1);

        assertThat(result.get("FIRST")).isEqualTo(1L * Rank.FIRST.getPrize());
        assertThat(result.get("SECOND")).isEqualTo(2L * Rank.SECOND.getPrize());
        assertThat(result.get("TOTAL")).isEqualTo(
                Rank.FIRST.getPrize() * 1L +
                        Rank.SECOND.getPrize() * 2L +
                        Rank.THIRD.getPrize() * 3L +
                        Rank.FOURTH.getPrize() * 4L +
                        Rank.FIFTH.getPrize() * 5L
        );

        verify(roundRepository).findByRoundId(1);
    }

    @Test
    @DisplayName("calculateLottoSize() → 모든 티켓의 Lotto 개수를 합산한다")
    void calculateLottoSize_success() {

        Lotto lottoA = Lotto.of(List.of(
                LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
        ));

        Lotto lottoB = Lotto.of(List.of(
                LottoNumber.of(7), LottoNumber.of(8), LottoNumber.of(9),
                LottoNumber.of(10), LottoNumber.of(11), LottoNumber.of(12)
        ));

        LottoTicket ticket1 = LottoTicket.of(1, List.of(lottoA, lottoB));
        LottoTicket ticket2 = LottoTicket.of(1, List.of(lottoA));

        when(lottoTicketRepository.findTicketsByRoundId(1))
                .thenReturn(List.of(ticket1, ticket2));

        long count = service.calculateLottoSize(1);

        assertThat(count).isEqualTo(3);
        verify(lottoTicketRepository).findTicketsByRoundId(1);
    }
}
