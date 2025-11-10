package lotto.domain.vo;

public class CountResult {
    private final int numberMatchCount;
    private final boolean BonusNumberMatch;

    public CountResult(int numberMatchCount, boolean bonusNumberMatch) {
        this.numberMatchCount = numberMatchCount;
        this.BonusNumberMatch = bonusNumberMatch;
    }

    public int getNumberMatchCount() {
        return numberMatchCount;
    }

    public boolean getBonusNumberMatchCount() {
        return BonusNumberMatch;
    }
}
