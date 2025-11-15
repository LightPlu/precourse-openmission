package lotto.domain.vo;

import static lotto.domain.NumberConstants.LOTTO_GAME_MAX_PURCHASE;
import static lotto.domain.NumberConstants.LOTTO_PURCHASE_MAX_MONEY;
import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_DIVISIBLE;
import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_POSITIVE;
import static lotto.domain.NumberConstants.LOTTO_PRICE;
import static lotto.domain.NumberConstants.ZERO;
import static lotto.exceptions.ErrorMessage.CASH_RANGE_OUT;

public class Cash {
    private final int money;
    private final int fullGameCount;
    private final int remainingGameCount;

    public Cash(int money) {
        validateMoneyPositive(money);
        validateMoneyRange(money);
        validateMoneyDivisible(money);
        this.money = money;
        this.fullGameCount = calculateFullGameCount(money);
        this.remainingGameCount = calculateRemainingGameCount(money);
    }

    public static Cash of(int money) {
        return new Cash(money);
    }

    private void validateMoneyDivisible(int money) {
        if (money % LOTTO_PRICE.getValue() != ZERO.getValue()) {
            throw new IllegalArgumentException(CASH_IS_NOT_DIVISIBLE.getMessage());
        }
    }

    private void validateMoneyPositive(int money) {
        if (money <= ZERO.getValue()) {
            throw new IllegalArgumentException(CASH_IS_NOT_POSITIVE.getMessage());
        }
    }

    private void validateMoneyRange(int money) {
        if (money > LOTTO_PURCHASE_MAX_MONEY.getValue()) {
            throw new IllegalArgumentException(CASH_RANGE_OUT.getMessage());
        }
    }

    private int calculateFullGameCount(int money) {
        return money / (LOTTO_GAME_MAX_PURCHASE.getValue() * LOTTO_PRICE.getValue());
    }

    private int calculateRemainingGameCount(int money) {
        int eachMoney = money % (LOTTO_GAME_MAX_PURCHASE.getValue() * LOTTO_PRICE.getValue());
        return eachMoney / LOTTO_PRICE.getValue();
    }

    public int getMoney() {
        return money;
    }

    public int getFullGameCount() {
        return fullGameCount;
    }

    public int getRemainingGameCount() {
        return remainingGameCount;
    }
}
