package lotto.domain.vo;

import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_DIVISIBLE;
import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_POSITIVE;
import static lotto.utils.NumberConstants.LOTTO_PRICE;
import static lotto.utils.NumberConstants.ZERO;

public class Cash {
    private final int money;

    public Cash(int money) {
        validateMoneyPositive(money);
        validateMoneyDivisible(money);
        this.money = money;
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

    public int getMoney() {
        return money;
    }
}
