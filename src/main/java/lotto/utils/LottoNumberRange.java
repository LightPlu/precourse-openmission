package lotto.utils;

public enum LottoNumberRange {

    MIN_LOTTO_NUMBER(1),
    MAX_LOTTO_NUMBER(45),
    OUTPUT_LOTTO_NUMBER_COUNT(6);

    public final int number;

    LottoNumberRange(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}
