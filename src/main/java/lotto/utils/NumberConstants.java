package lotto.utils;

public enum NumberConstants {

    LOTTO_PRICE(1000),
    LOTTO_SIZE(6),
    ZERO(0),
    PERCENTAGE(100);

    private final int value;

    NumberConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
