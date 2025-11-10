package lotto.domain.vo;

import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_DIVISIBLE;
import static lotto.exceptions.ErrorMessage.CASH_IS_NOT_POSITIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CashTest {

    @DisplayName("정상적인 금액으로 Cash 객체를 생성한다")
    @Test
    void createCash() {
        int money = 8000;

        Cash cash = new Cash(money);

        assertThat(cash.getMoney()).isEqualTo(money);
    }

    @DisplayName("0원 이하의 금액으로 Cash 객체 생성 시 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1000, -100})
    void createCashWithNegativeMoney(int money) {

        assertThatThrownBy(() -> new Cash(money))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CASH_IS_NOT_POSITIVE.getMessage());
    }

    @DisplayName("1000원으로 나누어떨어지지 않는 금액으로 Cash 객체 생성 시 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {500, 1500, 2300, 9999})
    void createCashWithNotDivisibleMoney(int money) {

        assertThatThrownBy(() -> new Cash(money))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CASH_IS_NOT_DIVISIBLE.getMessage());
    }
}

