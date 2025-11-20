package lotto.application;

import static lotto.application.common.ServiceErrorMessage.NOT_FINISHED_ROUND;

import java.util.List;
import java.util.Map;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.repository.RoundRepository;

public class RoundResultApplicationServiceImpl implements RoundResultApplicationService {

    private final RoundRepository roundRepository;
    private final LottoTicketRepository lottoTicketRepository;

    public RoundResultApplicationServiceImpl(
            LottoTicketRepository lottoTicketRepository,
            RoundRepository roundRepository) {
        this.lottoTicketRepository = lottoTicketRepository;
        this.roundRepository = roundRepository;
    }

    @Override
    public Map<String, Integer> getRoundResult(int roundNumber) {
        RoundResult result = roundRepository.findRoundResultByRoundId(roundNumber)
                .orElseThrow(() -> new RuntimeException(NOT_FINISHED_ROUND.getMessage()));
        return result.toMap();
    }

    @Override
    public Map<String, Long> calculateWinningPrize(int roundNumber) {
        RoundResult result = roundRepository.findRoundResultByRoundId(roundNumber)
                .orElseThrow(() -> new RuntimeException(NOT_FINISHED_ROUND.getMessage()));
        return result.calculateWinningPrize();
    }

    @Override
    public long calculateLottoSize(int roundNumber) {
        List<LottoTicket> tickets = lottoTicketRepository.findTicketsByRoundId(roundNumber);
        return tickets.stream()
                .mapToLong(LottoTicket::calculateLottoSize)
                .sum();
    }
}
