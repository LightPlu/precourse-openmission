package lotto.service;

import java.util.List;
import lotto.domain.entity.LottoTicket;
import lotto.domain.entity.Round;
import lotto.domain.repository.RoundRepository;
import lotto.domain.service.LottoPurchaseService;
import lotto.domain.vo.Cash;
import lotto.domain.vo.Lotto;

public class TicketApplicationServiceImpl implements TicketApplicationService {

    private final RoundRepository roundRepository;
    private final LottoPurchaseService lottoPurchaseService;

    public TicketApplicationServiceImpl(RoundRepository roundRepository, LottoPurchaseService lottoPurchaseService) {
        this.lottoPurchaseService = lottoPurchaseService;
        this.roundRepository = roundRepository;
    }

    @Override
    public void buyTicketsAndSave(int money) {
        Cash cash = Cash.of(money);
        List<List<Lotto>> purchased = lottoPurchaseService.purchase(cash);

        Round round = roundRepository.findLatestRound()
                .orElseThrow(() -> new IllegalStateException("해당 회차가 존재하지 않습니다."));

        int roundId = round.getId();  // PK 가져오기

        List<LottoTicket> tickets =
                purchased.stream()
                        .map(lottos -> LottoTicket.of(roundId, lottos))
                        .toList();

        roundRepository.saveTickets(tickets);
    }

}
