package lotto.application;

import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.lottoTicket.vo.LottoNumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static lotto.application.common.ServiceErrorMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WinningLottoApplicationServiceImplTest {

    private RoundRepository roundRepository;
    private WinningLottoApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        roundRepository = mock(RoundRepository.class);
        service = new WinningLottoApplicationServiceImpl(roundRepository);
    }

    private List<LottoNumber> nums(List<Integer> numbers) {
        return numbers.stream()
                .map(LottoNumber::of)
                .toList();
    }

    @Test
    @DisplayName("현재 회차가 없으면 당첨번호 저장 시 예외 발생")
    void saveWinningNumbers_noRound() {

        when(roundRepository.findLatestRound()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveWinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NO_ROUND.getMessage());
    }

    @Test
    @DisplayName("이미 당첨번호가 등록된 경우 예외 발생")
    void saveWinningNumbers_alreadyRegistered() {
        Round round = new Round(3, 3);
        when(roundRepository.findLatestRound()).thenReturn(Optional.of(round));
        when(roundRepository.findWinningLottoNumbersByRoundId(3))
                .thenReturn(Optional.of(mock(WinningLottoNumbers.class)));

        assertThatThrownBy(() -> service.saveWinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(REGISTERED_WINNING_NUMBERS.getMessage());
    }

    @Test
    @DisplayName("당첨번호와 보너스 번호를 정상 저장한다")
    void saveWinningNumbers_success() {
        // given
        Round round = new Round(5, 5);
        when(roundRepository.findLatestRound()).thenReturn(Optional.of(round));
        when(roundRepository.findWinningLottoNumbersByRoundId(5)).thenReturn(Optional.empty());

        service.saveWinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);

        verify(roundRepository).saveWinningLottoNumbers(any(WinningLottoNumbers.class));
    }

    @Test
    @DisplayName("getWinningNumbers는 해당 회차 당첨 정보가 없으면 예외 발생")
    void getWinningNumbers_notFound() {
        when(roundRepository.findWinningLottoNumbersByRoundId(10))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getWinningNumbers(10))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NOT_REGISTERED_WINNING_NUMBERS.getMessage());
    }

    @Test
    @DisplayName("getWinningNumbers는 형식에 맞는 문자열을 반환")
    void getWinningNumbers_success() {
        WinningLottoNumbers w = WinningLottoNumbers.of(
                0,
                nums(List.of(1, 2, 3, 4, 5, 6)),
                LottoNumber.of(10),
                7
        );

        when(roundRepository.findWinningLottoNumbersByRoundId(7))
                .thenReturn(Optional.of(w));

        String result = service.getWinningNumbers(7);

        assertThat(result).isEqualTo(
                "당첨번호 : 1,2,3,4,5,6 | 보너스 번호 : 10"
        );
    }
}
