package lotto.domain.round.repository;

import java.util.List;
import java.util.Optional;

import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;

public interface RoundRepository {

    Optional<Round> findLatestRound();

    Optional<Round> findByRoundNumber(int roundNumber);

    Round saveRound(Round round);

    void saveWinningLottoNumbers(WinningLottoNumbers winningNumbers);

    Optional<WinningLottoNumbers> findWinningLottoNumbersByRoundId(int roundId);

    void saveRoundResult(RoundResult result);

    Optional<RoundResult> findRoundResultByRoundId(int roundId);

    List<Integer> findAllRoundNumbers();
}
