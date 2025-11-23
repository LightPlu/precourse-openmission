package lotto.application.roundService;

import static lotto.application.common.ServiceErrorMessage.NOT_FINISHED_ROUND;

import java.util.List;
import java.util.Map;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.entity.Round;
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
    public Map<String, Integer> getRoundResult(int roundId) {
        Round round = roundRepository.findByRoundId(roundId)
                .orElseThrow(() -> new RuntimeException(NOT_FINISHED_ROUND.getMessage()));

        RoundResult result = round.getRoundResult();
        return result.toMap();
    }

    @Override
    public Map<String, Long> calculateWinningPrize(int roundId) {
        Round round = roundRepository.findByRoundId(roundId)
                .orElseThrow(() -> new RuntimeException(NOT_FINISHED_ROUND.getMessage()));

        RoundResult result = round.getRoundResult();
        return result.calculateWinningPrize();
    }

    @Override
    public long calculateLottoSize(int roundId) {
        List<LottoTicket> tickets = lottoTicketRepository.findTicketsByRoundId(roundId);
        return tickets.stream()
                .mapToLong(LottoTicket::calculateLottoSize)
                .sum();
    }
}
