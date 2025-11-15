package lotto.domain.repository;

import java.util.List;
import java.util.Optional;

import lotto.domain.entity.LottoTicket;
import lotto.domain.entity.Round;
import lotto.domain.entity.RoundResult;
import lotto.domain.entity.WinningLottoNumbers;

public interface RoundRepository {

    Optional<Round> findLatestRound();

    Optional<Round> findByRoundNumber(int roundNumber);

    Round saveRound(Round round);

    List<LottoTicket> findTicketsByRoundId(int roundId);

    void saveTickets(List<LottoTicket> ticket);

    void saveWinningLottoNumbers(WinningLottoNumbers winningNumbers);

    Optional<WinningLottoNumbers> findWinningLottoNumbersByRoundId(int roundId);

    RoundResult saveRoundResult(RoundResult result);

    Optional<RoundResult> findRoundResultByRoundId(int roundId);

    List<Integer> findAllRoundNumbers();
}
