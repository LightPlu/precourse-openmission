package lotto.domain.round.entity;

import static lotto.domain.common.DomainErrorMessage.CANT_REGISTER_WITH_NO_WINNING_NUMBER;
import static lotto.domain.common.DomainErrorMessage.RESULT_REGISTERED;
import static lotto.domain.common.DomainErrorMessage.ROUND_IS_RUNNING;
import static lotto.domain.common.DomainErrorMessage.WINNING_NUMBER_REGISTERED;

import java.util.Objects;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;

public class Round {

    private final int id;
    private final int roundNumber;

    private WinningLottoNumbers winningLottoNumbers;
    private RoundResult roundResult;

    public Round(int id, int roundNumber) {
        this.id = id;
        this.roundNumber = roundNumber;
    }

    public static Round of(int roundNumber) {
        return new Round(0, roundNumber);
    }

    public int getId() {
        return id;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void registerWinningNumbers(WinningLottoNumbers winningNumbers) {
        if (this.winningLottoNumbers != null) {
            throw new IllegalStateException(WINNING_NUMBER_REGISTERED.getMessage());
        }
        this.winningLottoNumbers = winningNumbers;
    }

    public void registerRoundResult(RoundResult result) {
        if (this.winningLottoNumbers == null) {
            throw new IllegalStateException(CANT_REGISTER_WITH_NO_WINNING_NUMBER.getMessage());
        }
        if (this.roundResult != null) {
            throw new IllegalStateException(RESULT_REGISTERED.getMessage());
        }
        this.roundResult = result;
    }

    public WinningLottoNumbers getWinningLottoNumbers() {
        return winningLottoNumbers;
    }

    public RoundResult getRoundResult() {
        if (roundResult == null) {
            throw new IllegalStateException(ROUND_IS_RUNNING.getMessage());
        }
        return roundResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Round)) {
            return false;
        }
        Round round = (Round) o;
        return roundNumber == round.roundNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundNumber);
    }
}
