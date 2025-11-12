package lotto.domain.vo;

import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.exceptions.ErrorMessage.WINNING_NUMBER_DUPLICATE_BONUS_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import lotto.domain.entity.WinningLottoNumbers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WinningLottoNumbersTest {

    @DisplayName("정상적인 당첨 번호와 보너스 번호로 WinningLottoNumbers 객체를 생성한다")
    @Test
    void createWinningLottoNumbers() {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningLottoNumbers winningLottoNumbers = new WinningLottoNumbers(winningNumbers, bonusNumber);

        assertThat(winningLottoNumbers.getWinningNumbers()).isEqualTo(winningNumbers);
        assertThat(winningLottoNumbers.getBonusNumber()).isEqualTo(bonusNumber);
    }

    @DisplayName("보너스 번호가 1보다 작으면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void createWithBonusNumberLessThanMin(int bonusNumber) {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        assertThatThrownBy(() -> new WinningLottoNumbers(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @DisplayName("보너스 번호가 45보다 크면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {46, 50, 100})
    void createWithBonusNumberGreaterThanMax(int bonusNumber) {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        assertThatThrownBy(() -> new WinningLottoNumbers(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @DisplayName("당첨 번호에 1보다 작은 숫자가 포함되면 예외가 발생한다")
    @Test
    void createWithWinningNumberLessThanMin() {
        List<Integer> winningNumbers = List.of(0, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        assertThatThrownBy(() -> new WinningLottoNumbers(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @DisplayName("당첨 번호에 45보다 큰 숫자가 포함되면 예외가 발생한다")
    @Test
    void createWithWinningNumberGreaterThanMax() {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 46);
        int bonusNumber = 7;

        assertThatThrownBy(() -> new WinningLottoNumbers(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @DisplayName("당첨 번호와 보너스 번호가 중복되면 예외가 발생한다")
    @Test
    void createWithDuplicateBonusNumber() {
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 6;

        assertThatThrownBy(() -> new WinningLottoNumbers(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(WINNING_NUMBER_DUPLICATE_BONUS_NUMBER.getMessage());
    }
}

