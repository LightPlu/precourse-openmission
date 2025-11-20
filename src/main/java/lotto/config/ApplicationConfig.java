package lotto.config;

import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.infrastructure.repository.PostgresLottoTicketRepository;
import lotto.presentation.controller.LottoController;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.application.LottoPurchaseService;
import lotto.domain.service.LottoNumberGenerator;
import lotto.infrastructure.RandomNumberGenerator;
import lotto.infrastructure.RandomNumberGeneratorImpl;
import lotto.infrastructure.repository.PostgresRoundRepository;
import lotto.application.*;

public class ApplicationConfig {

    public LottoController lottoController() {

        RoundRepository roundRepository = new PostgresRoundRepository();
        LottoTicketRepository lottoTicketRepository = new PostgresLottoTicketRepository();

        RandomNumberGenerator generator = new RandomNumberGeneratorImpl();
        LottoNumberGenerator domainGenerator = new LottoNumberGenerator(generator);

        LottoPurchaseService purchaseService = new LottoPurchaseService(domainGenerator);
        LottoCompareService compareService = new LottoCompareService();

        RoundApplicationService roundService =
                new RoundApplicationServiceImpl(lottoTicketRepository, roundRepository, compareService);

        TicketApplicationService ticketService =
                new TicketApplicationServiceImpl(lottoTicketRepository, roundRepository, purchaseService);

        WinningLottoApplicationService winningService =
                new WinningLottoApplicationServiceImpl(roundRepository);

        RoundResultApplicationService resultService =
                new RoundResultApplicationServiceImpl(lottoTicketRepository, roundRepository);

        return new LottoController(
                roundService,
                ticketService,
                winningService,
                resultService
        );
    }
}
