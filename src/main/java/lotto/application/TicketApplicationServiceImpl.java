package lotto.application;

import java.util.List;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.entity.Round;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.vo.Cash;
import lotto.domain.lottoTicket.vo.Lotto;

public class TicketApplicationServiceImpl implements TicketApplicationService {

    private final LottoTicketRepository lottoTicketRepository;
    private final RoundRepository roundRepository;
    private final LottoPurchaseService lottoPurchaseService;

    public TicketApplicationServiceImpl(
            LottoTicketRepository lottoTicketRepository,
            RoundRepository roundRepository,
            LottoPurchaseService lottoPurchaseService) {
        this.lottoTicketRepository = lottoTicketRepository;
        this.roundRepository = roundRepository;
        this.lottoPurchaseService = lottoPurchaseService;
    }

    @Override
    public void buyTicketsAndSave(int money) {
        Cash cash = Cash.of(money);
        List<List<Lotto>> purchased = lottoPurchaseService.purchase(cash);

        Round round = checkOrCreateRound();

        int roundId = round.getId();  // PK 가져오기

        List<LottoTicket> tickets =
                purchased.stream()
                        .map(lottos -> LottoTicket.of(roundId, lottos))
                        .toList();

        lottoTicketRepository.saveTickets(tickets);
    }

    private Round checkOrCreateRound() {

        return roundRepository.findLatestRound()
                .orElseGet(() -> {
                    Round newRound = Round.of(1);
                    return roundRepository.saveRound(newRound);
                });
    }

}
