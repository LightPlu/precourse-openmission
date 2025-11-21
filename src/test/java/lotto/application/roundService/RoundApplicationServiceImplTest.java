package lotto.application.roundService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static lotto.application.common.ServiceErrorMessage.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.vo.Rank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class RoundApplicationServiceImplTest {

    RoundRepository roundRepository;
    LottoTicketRepository lottoTicketRepository;

    LottoCompareService compareService;

    @InjectMocks
    RoundApplicationServiceImpl service;

    @BeforeEach
    void setup() {
        roundRepository = mock(RoundRepository.class);
        lottoTicketRepository = mock(LottoTicketRepository.class);
        compareService = mock(LottoCompareService.class);
        service = new RoundApplicationServiceImpl(lottoTicketRepository, roundRepository, compareService);
    }

    @Test
    @DisplayName("getLatestRound()는 최신 라운드의 ID를 반환한다")
    void getLatestRound_success() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(5, 3)));

        int id = service.getLatestRound();

        assertThat(id).isEqualTo(5);
        verify(roundRepository).findLatestRound();
    }

    @Test
    @DisplayName("getLatestRound()는 라운드가 없다면 예외 발생")
    void getLatestRound_fail() {
        when(roundRepository.findLatestRound()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getLatestRound())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NO_ROUND.getMessage());
    }

    @Test
    @DisplayName("startNewRound(): 기존 라운드가 있을 경우 다음 번호로 생성")
    void startNewRound_existingRound() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(3, 3)));

        Round saved = new Round(4, 4);
        when(roundRepository.saveRound(any())).thenReturn(saved);

        Round result = service.startNewRound();

        assertThat(result.getRoundNumber()).isEqualTo(4);
        verify(roundRepository).saveRound(any());
    }

    @Test
    @DisplayName("startNewRound(): 첫 번째 라운드 생성")
    void startNewRound_firstRound() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.empty());

        Round saved = new Round(1, 1);
        when(roundRepository.saveRound(any())).thenReturn(saved);

        Round result = service.startNewRound();

        assertThat(result.getRoundNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 정상적으로 결과를 저장하고 다음 라운드 시작")
    void closeRoundAndStartNextRound_success() {

        int roundId = 10;

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(roundId, 5)));

        LottoTicket ticket = LottoTicket.of(
                roundId,
                List.of(Lotto.of(List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                )))
        );
        when(lottoTicketRepository.findTicketsByRoundId(roundId))
                .thenReturn(List.of(ticket));

        Round loaded = new Round(roundId, 5);
        WinningLottoNumbers winning = WinningLottoNumbers.of(
                99,
                List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ),
                LottoNumber.of(7),
                roundId
        );
        loaded.registerWinningNumbers(winning);

        when(roundRepository.findByRoundId(roundId))
                .thenReturn(Optional.of(loaded));

        when(compareService.compareTicket(any(), any()))
                .thenReturn(Map.of(Rank.FIRST, 1));

        when(roundRepository.saveRound(any()))
                .thenReturn(new Round(11, 6));

        service.closeRoundAndStartNextRound();

        verify(roundRepository).saveRoundResult(any(RoundResult.class));
        verify(roundRepository).saveRound(any());
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 티켓이 없으면 예외 발생")
    void closeRoundAndStartNextRound_noTickets() {

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(10, 3)));

        when(lottoTicketRepository.findTicketsByRoundId(10))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.closeRoundAndStartNextRound())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NO_TICKETS.getMessage());
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 당첨 번호 미등록이면 예외 발생")
    void closeRoundAndStartNextRound_noWinningNumbers() {

        int roundId = 10;

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(roundId, 3)));

        LottoTicket ticket = LottoTicket.of(
                roundId,
                List.of(Lotto.of(List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                )))
        );
        when(lottoTicketRepository.findTicketsByRoundId(roundId))
                .thenReturn(List.of(ticket));

        Round loaded = new Round(roundId, 3);

        when(roundRepository.findByRoundId(roundId))
                .thenReturn(Optional.of(loaded));

        assertThatThrownBy(() -> service.closeRoundAndStartNextRound())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NOT_REGISTERED_WINNING_NUMBERS.getMessage());
    }

}
