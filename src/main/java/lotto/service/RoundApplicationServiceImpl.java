package lotto.service;

import static lotto.service.ServiceErrorMessage.NOT_REGISTERED_WINNING_NUMBERS;
import static lotto.service.ServiceErrorMessage.NO_ROUND;
import static lotto.service.ServiceErrorMessage.NO_TICKETS;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lotto.domain.entity.LottoTicket;
import lotto.domain.entity.Round;
import lotto.domain.entity.RoundResult;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.domain.vo.PrizeDetail;
import lotto.domain.vo.Rank;

public class RoundApplicationServiceImpl implements RoundApplicationService {

    private final LottoCompareService lottoCompareService;
    private final RoundRepository roundRepository;

    public RoundApplicationServiceImpl(RoundRepository roundRepository, LottoCompareService lottoCompareService) {
        this.lottoCompareService = lottoCompareService;
        this.roundRepository = roundRepository;
    }

    @Override
    public int getLatestRound() {
        Round round = roundRepository.findLatestRound()
                .orElseThrow(() -> new IllegalStateException(NO_ROUND.getMessage()));
        return round.getRoundNumber();
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

        int roundId = getLatestRound();

        List<LottoTicket> tickets =
                roundRepository.findTicketsByRoundId(roundId);

        if (tickets.isEmpty()) {
            throw new IllegalStateException(NO_TICKETS.getMessage());
        }

        WinningLottoNumbers winning =
                roundRepository.findWinningLottoNumbersByRoundId(roundId)
                        .orElseThrow(() -> new IllegalStateException(NOT_REGISTERED_WINNING_NUMBERS.getMessage()));

        // 4) Rank별 count 계산
        Map<Rank, Integer> counts = new EnumMap<>(Rank.class);
        for (Rank r : Rank.values()) {
            counts.put(r, 0);
        }

        for (LottoTicket ticket : tickets) {
            List<PrizeDetail> details =
                    lottoCompareService.compareTicket(ticket, winning);

            for (PrizeDetail d : details) {
                counts.put(
                        d.getRank(),
                        counts.get(d.getRank()) + d.getCount()
                );
            }
        }

        // 5) 결과 저장
        RoundResult result = RoundResult.of(0, roundId, counts);
        RoundResult savedResult = roundRepository.saveRoundResult(result);

        // 6) 다음 회차 생성
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

}
