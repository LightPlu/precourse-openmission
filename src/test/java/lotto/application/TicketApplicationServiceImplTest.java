package lotto.application;

import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.round.entity.Round;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.vo.Cash;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketApplicationServiceImplTest {

    private RoundRepository roundRepository;
    private LottoPurchaseService lottoPurchaseService;
    private TicketApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        roundRepository = mock(RoundRepository.class);
        lottoPurchaseService = mock(LottoPurchaseService.class);
        service = new TicketApplicationServiceImpl(roundRepository, lottoPurchaseService);
    }

    private Lotto lotto(List<Integer> nums) {
        return Lotto.of(
                nums.stream()
                        .map(LottoNumber::of)
                        .toList()
        );
    }

    @Test
    @DisplayName("라운드가 없으면 1회차를 생성하고 티켓을 저장한다")
    void buyTicketsAndSave_createRoundIfNotExists() {
        when(roundRepository.findLatestRound()).thenReturn(Optional.empty());

        Round createdRound = Round.of(1);
        when(roundRepository.saveRound(any(Round.class)))
                .thenReturn(new Round(1, 1));

        List<List<Lotto>> purchased = List.of(
                List.of(lotto(List.of(1, 2, 3, 4, 5, 6))),
                List.of(lotto(List.of(7, 8, 9, 10, 11, 12)))
        );

        when(lottoPurchaseService.purchase(any(Cash.class)))
                .thenReturn(purchased);

        service.buyTicketsAndSave(5000);

        verify(roundRepository).saveRound(any(Round.class));
        verify(roundRepository).saveTickets(anyList());
    }

    @Test
    @DisplayName("라운드가 존재하면 새로운 라운드를 생성하지 않고 기존 라운드에 티켓 저장")
    void buyTicketsAndSave_useExistingRound() {

        Round existingRound = new Round(3, 3);
        when(roundRepository.findLatestRound()).thenReturn(Optional.of(existingRound));

        List<List<Lotto>> purchased = List.of(
                List.of(lotto(List.of(1, 2, 3, 4, 5, 6))),
                List.of(lotto(List.of(7, 8, 9, 10, 11, 12)))
        );
        when(lottoPurchaseService.purchase(any(Cash.class)))
                .thenReturn(purchased);

        service.buyTicketsAndSave(5000);

        verify(roundRepository, never()).saveRound(any());
        verify(roundRepository).saveTickets(anyList());
    }

    @Test
    @DisplayName("생성된 LottoTicket들이 RoundId를 올바르게 갖는지 확인")
    void buyTicketsAndSave_roundIdAppliedToTickets() {
        Round existing = new Round(5, 5);
        when(roundRepository.findLatestRound()).thenReturn(Optional.of(existing));

        List<List<Lotto>> purchased = List.of(
                List.of(lotto(List.of(1, 2, 3, 4, 5, 6)))
        );

        when(lottoPurchaseService.purchase(any()))
                .thenReturn(purchased);

        List<LottoTicket> captureTickets = new ArrayList<>();
        doAnswer(invocation -> {
            List<LottoTicket> list = invocation.getArgument(0);
            captureTickets.addAll(list);
            return null;
        }).when(roundRepository).saveTickets(anyList());

        service.buyTicketsAndSave(5000);

        assertThat(captureTickets).hasSize(1);
        assertThat(captureTickets.getFirst().getRoundId()).isEqualTo(5);
    }
}
