package lotto.application;

import static lotto.application.common.ServiceErrorMessage.NOT_REGISTERED_WINNING_NUMBERS;
import static lotto.application.common.ServiceErrorMessage.NO_ROUND;
import static lotto.application.common.ServiceErrorMessage.NO_TICKETS;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.domain.vo.Rank;

public class RoundApplicationServiceImpl implements RoundApplicationService {

    private final LottoCompareService lottoCompareService;
    private final RoundRepository roundRepository;
    private final LottoTicketRepository lottoTicketRepository;

    public RoundApplicationServiceImpl(
            LottoTicketRepository lottoTicketRepository,
            RoundRepository roundRepository,
            LottoCompareService lottoCompareService) {
        this.lottoCompareService = lottoCompareService;
        this.roundRepository = roundRepository;
        this.lottoTicketRepository = lottoTicketRepository;
    }

    @Override
    public int getLatestRound() {
        Round round = roundRepository.findLatestRound()
                .orElseThrow(() -> new IllegalStateException(NO_ROUND.getMessage()));
        return round.getId();
    }

    @Override
    public Round startNewRound() {
        int nextNumber = roundRepository.findLatestRound()
                .map(r -> r.getRoundNumber() + 1)
                .orElse(1);

        Round newRound = Round.of(nextNumber);
        return roundRepository.saveRound(newRound);
    }

    @Override
    public void closeRoundAndStartNextRound() {
        int roundId = getValidRoundIdWithTickets();
        Round round = roundRepository.findByRoundId(roundId)
                .orElseThrow(() -> new IllegalStateException(NOT_REGISTERED_WINNING_NUMBERS.getMessage()));

        if(round.getWinningLottoNumbers() == null) {
            throw new IllegalStateException(NOT_REGISTERED_WINNING_NUMBERS.getMessage());
        }

        roundRepository.saveRoundResult(RoundResult.of(
                0,
                roundId,
                aggregateRankCounts(lottoTicketRepository.findTicketsByRoundId(roundId), round.getWinningLottoNumbers())
        ));
        startNewRound();
    }

    @Override
    public List<Integer> findAllRoundNumbers() {
        return roundRepository.findAllRoundNumbers();
    }

    @Override
    public int findRoundIdByRoundNumber(int roundNumber) {
        Round round = roundRepository.findByRoundNumber(roundNumber)
                .orElseThrow(() -> new IllegalStateException(NO_ROUND.getMessage()));
        return round.getId();
    }

    private Map<Rank, Integer> aggregateRankCounts(List<LottoTicket> tickets, WinningLottoNumbers winning) {
        Map<Rank, Integer> counts = new EnumMap<>(Rank.class);
        for (Rank r : Rank.values()) {
            counts.put(r, 0);
        }

        for (LottoTicket ticket : tickets) {
            Map<Rank, Integer> each = lottoCompareService.compareTicket(ticket, winning);

            each.forEach((rank, count) ->
                    counts.put(rank, counts.get(rank) + count)
            );
        }
        return counts;
    }

    private int getValidRoundIdWithTickets() {
        int roundId = getLatestRound();
        List<LottoTicket> tickets = lottoTicketRepository.findTicketsByRoundId(roundId);

        if (tickets.isEmpty()) {
            throw new IllegalStateException(NO_TICKETS.getMessage());
        }
        return roundId;
    }
}
