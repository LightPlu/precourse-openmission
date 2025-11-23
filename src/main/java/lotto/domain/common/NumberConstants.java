package lotto.domain.common;

public enum NumberConstants {

    LOTTO_PRICE(1000),
    LOTTO_NUMBER_SIZE(6),
    LOTTO_GAME_MAX_PURCHASE(5),
    LOTTO_PURCHASE_MAX_MONEY(100000),
    LOTTO_START_NUMBER(1),
    LOTTO_END_NUMBER(45),
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
