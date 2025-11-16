package lotto.domain.vo;

import static lotto.domain.DomainErrorMessage.CASH_IS_NOT_DIVISIBLE;
import static lotto.domain.DomainErrorMessage.CASH_IS_NOT_POSITIVE;
import static lotto.domain.DomainErrorMessage.CASH_RANGE_OUT;
import static lotto.domain.NumberConstants.LOTTO_PURCHASE_MAX_MONEY;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;

class CashTest {

    @Test
    @DisplayName("정상적으로 Cash 생성 시 fullGameCount와 remainingGameCount가 올바르게 계산된다")
    void createCashSuccess() {
        // given
        int money = 7000;  // 5000(5게임) + 2000(2게임)

        // when
        Cash cash = Cash.of(money);

        // then
        assertThat(cash.getFullGameCount()).isEqualTo(1); // 5000원짜리 티켓 1개
        assertThat(cash.getRemainingGameCount()).isEqualTo(2); // 남은 2000원 → 2게임
        assertThat(cash.getMoney()).isEqualTo(7000);
    }

    @ParameterizedTest
    @ValueSource(ints = {15300, 1499, 35300, 6870})
    @DisplayName("금액이 1000원 단위가 아니면 예외가 발생한다")
    void moneyMustBeDivisibleBy1000(int money) {
        // when & then
        assertThatThrownBy(() -> Cash.of(money))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CASH_IS_NOT_DIVISIBLE.getMessage());
    }

    @Test
    @DisplayName("금액이 0 이하이면 예외가 발생한다")
    void moneyMustBePositive() {
        assertThatThrownBy(() -> Cash.of(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CASH_IS_NOT_POSITIVE.getMessage());

        assertThatThrownBy(() -> Cash.of(-1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CASH_IS_NOT_POSITIVE.getMessage());
    }

    @Test
    @DisplayName("금액이 최대 구매 금액을 초과하면 예외가 발생한다")
    void moneyExceedsMaxPurchase() {
        int overLimit = LOTTO_PURCHASE_MAX_MONEY.getValue() + 1000;

        assertThatThrownBy(() -> Cash.of(overLimit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CASH_RANGE_OUT.getMessage());
    }

    @Test
    @DisplayName("그 외 정상적인 금액은 예외 없이 Cash가 생성된다")
    void validMoney() {
        assertThatCode(() -> Cash.of(1000))
                .doesNotThrowAnyException();

        assertThatCode(() -> Cash.of(45000))
                .doesNotThrowAnyException();
    }
}
