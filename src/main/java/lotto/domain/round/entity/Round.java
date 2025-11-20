package lotto.domain.round.entity;

import java.util.Objects;

public class Round {

    private final int id;
    private final int roundNumber;

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
