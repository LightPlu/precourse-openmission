package lotto.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static lotto.application.common.ServiceErrorMessage.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lotto.application.roundService.RoundApplicationServiceImpl;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.WinningLottoNumbers;
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

    @Mock
    RoundRepository roundRepository;

    @Mock
    LottoCompareService compareService;

    @InjectMocks
    RoundApplicationServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getLatestRound()는 최신 라운드의 ID를 반환한다")
    void getLatestRound_success() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(5, 3)));  // id=5, roundNumber=3

        int id = service.getLatestRound();

        assertThat(id).isEqualTo(5);
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
    @DisplayName("startNewRound()는 기존 라운드가 있을 경우 +1 한 번호로 신규 라운드를 생성한다")
    void startNewRound_existingRound() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(3, 3))); // current roundNumber = 3

        Round newRound = new Round(4, 4);
        when(roundRepository.saveRound(any())).thenReturn(newRound);

        Round result = service.startNewRound();

        assertThat(result.getRoundNumber()).isEqualTo(4);
        verify(roundRepository).saveRound(any());
    }

    @Test
    @DisplayName("startNewRound()는 라운드가 없다면 1회차로 시작한다")
    void startNewRound_firstRound() {
        when(roundRepository.findLatestRound())
                .thenReturn(Optional.empty());

        Round saved = new Round(1, 1);
        when(roundRepository.saveRound(any())).thenReturn(saved);

        Round result = service.startNewRound();

        assertThat(result.getRoundNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 정상적으로 결과 저장 후 다음 라운드 시작")
    void closeRoundAndStartNextRound_success() {

        int roundId = 10;
        Round round = new Round(roundId, 5);

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(round));

        LottoTicket ticket = LottoTicket.of(roundId, List.of(
                Lotto.of(List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ))
        ));
        when(roundRepository.findTicketsByRoundId(roundId))
                .thenReturn(List.of(ticket));

        WinningLottoNumbers winning = WinningLottoNumbers.of(
                0,
                List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ),
                LottoNumber.of(7),
                roundId
        );
        when(roundRepository.findWinningLottoNumbersByRoundId(roundId))
                .thenReturn(Optional.of(winning));

        when(compareService.compareTicket(any(), any()))
                .thenReturn(Map.of(Rank.FIRST, 1));

        when(roundRepository.saveRound(any()))
                .thenReturn(new Round(11, 6));

        service.closeRoundAndStartNextRound();

        verify(roundRepository).saveRoundResult(any());
        verify(roundRepository).saveRound(any());
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 티켓이 없다면 예외 발생")
    void closeRoundAndStartNextRound_noTickets() {

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(10, 3)));

        when(roundRepository.findTicketsByRoundId(10))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.closeRoundAndStartNextRound())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NO_TICKETS.getMessage());
    }

    @Test
    @DisplayName("closeRoundAndStartNextRound(): 당첨 번호 미등록이면 예외 발생")
    void closeRoundAndStartNextRound_noWinningNumbers() {

        when(roundRepository.findLatestRound())
                .thenReturn(Optional.of(new Round(10, 3)));

        LottoTicket ticket = LottoTicket.of(10, List.of(
                Lotto.of(List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ))
        ));
        when(roundRepository.findTicketsByRoundId(10))
                .thenReturn(List.of(ticket));

        when(roundRepository.findWinningLottoNumbersByRoundId(10))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.closeRoundAndStartNextRound())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NOT_REGISTERED_WINNING_NUMBERS.getMessage());
    }
}
