package lotto.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Round {

    private final Long id;
    private final int roundNumber;

    private WinningLottoNumbers winningLottoNumbers;
    private RoundResult roundResult;

    public Round(Long id, int roundNumber) {
        this.id = id;
        this.roundNumber = roundNumber;
    }

    public static Round of(int roundNumber) {
        return new Round(null, roundNumber);
    }

    public Long getId() {
        return id;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public WinningLottoNumbers getWinningLottoNumbers() {
        return winningLottoNumbers;
    }

    public void registerWinningNumbers(WinningLottoNumbers winningLottoNumbers) {
        if (this.winningLottoNumbers != null) {
            throw new IllegalStateException("이미 당첨 번호가 등록된 회차입니다.");
        }
        this.winningLottoNumbers = winningLottoNumbers;
    }

    public RoundResult getRoundResult() {
        return roundResult;
    }

    public void registerResult(RoundResult result) {
        if (this.roundResult != null) {
            throw new IllegalStateException("이미 결과가 등록된 회차입니다.");
        }
        this.roundResult = result;
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
