package lotto.domain.round.repository;

import java.util.List;
import java.util.Optional;

import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;

public interface RoundRepository {

    Optional<Round> findLatestRound();

    Optional<Round> findByRoundNumber(int roundNumber);

    Optional<Round> findByRoundId(int roundId);

    Round saveRound(Round round);

    void saveWinningLottoNumbers(WinningLottoNumbers winningNumbers);

    void saveRoundResult(RoundResult result);

    List<Integer> findAllRoundNumbers();
}
