package lotto.domain.round.entity;

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
            throw new IllegalStateException("이미 당첨 번호가 등록된 Round입니다.");
        }
        this.winningLottoNumbers = winningNumbers;
    }

    public void registerRoundResult(RoundResult result) {
        if (this.winningLottoNumbers == null) {
            throw new IllegalStateException("당첨 번호 없이 결과를 등록할 수 없습니다.");
        }
        if (this.roundResult != null) {
            throw new IllegalStateException("이미 결과가 등록된 Round입니다.");
        }
        this.roundResult = result;
    }

    public WinningLottoNumbers getWinningLottoNumbers() {
        return winningLottoNumbers;
    }

    public RoundResult getRoundResult() {
        if (roundResult == null) {
            throw new IllegalStateException("아직 종료되지 않은 회차입니다.");
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
