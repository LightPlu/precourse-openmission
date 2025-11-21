package lotto.config;

import lotto.application.lottoService.TicketApplicationService;
import lotto.application.lottoService.TicketApplicationServiceImpl;
import lotto.application.roundService.RoundApplicationService;
import lotto.application.roundService.RoundApplicationServiceImpl;
import lotto.application.roundService.RoundResultApplicationService;
import lotto.application.roundService.RoundResultApplicationServiceImpl;
import lotto.application.roundService.WinningLottoApplicationService;
import lotto.application.roundService.WinningLottoApplicationServiceImpl;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.infrastructure.repository.PostgresLottoTicketRepository;
import lotto.presentation.controller.LottoController;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.application.lottoService.LottoPurchaseService;
import lotto.domain.service.LottoNumberGenerator;
import lotto.infrastructure.RandomNumberGenerator;
import lotto.infrastructure.RandomNumberGeneratorImpl;
import lotto.infrastructure.repository.PostgresRoundRepository;

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
