package lotto.config;

import lotto.controller.LottoController;
import lotto.domain.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.domain.service.LottoPurchaseService;
import lotto.domain.service.LottoNumberGenerator;
import lotto.domain.service.RandomNumberGenerator;
import lotto.infrastructure.RandomNumberGeneratorImpl;
import lotto.infrastructure.repository.PostgresRoundRepository;
import lotto.service.*;

public class ApplicationConfig {

    public LottoController lottoController() {

        RoundRepository roundRepository = new PostgresRoundRepository();

        RandomNumberGenerator generator = new RandomNumberGeneratorImpl();
        LottoNumberGenerator domainGenerator = new LottoNumberGenerator(generator);

        LottoPurchaseService purchaseService = new LottoPurchaseService(domainGenerator);
        LottoCompareService compareService = new LottoCompareService();

        RoundApplicationService roundService =
                new RoundApplicationServiceImpl(roundRepository, compareService);

        TicketApplicationService ticketService =
                new TicketApplicationServiceImpl(roundRepository, purchaseService);

        WinningLottoApplicationService winningService =
                new WinningLottoApplicationServiceImpl(roundRepository);

        RoundResultApplicationService resultService =
                new RoundResultApplicationServiceImpl(roundRepository);

        return new LottoController(
                roundService,
                ticketService,
                winningService,
                resultService
        );
    }
}
