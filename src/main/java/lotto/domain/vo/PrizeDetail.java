package lotto.domain.vo;

import java.util.Objects;

public class PrizeDetail {

    private final Rank rank;
    private final int count;
    private final double totalPrize;

    private PrizeDetail(Rank rank, int count, double totalPrize) {
        this.rank = rank;
        this.count = count;
        this.totalPrize = totalPrize;
    }

    public static PrizeDetail of(Rank rank, int count) {
        double totalPrize = rank.getPrize() * count;
        return new PrizeDetail(rank, count, totalPrize);
    }

    public Rank getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }

    public double getTotalPrize() {
        return totalPrize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrizeDetail)) return false;
        PrizeDetail that = (PrizeDetail) o;
        return count == that.count &&
                Double.compare(that.totalPrize, totalPrize) == 0 &&
                rank == that.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, count, totalPrize);
    }
}

