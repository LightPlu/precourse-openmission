package lotto.domain.vo;

import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.exceptions.ErrorMessage.WINNING_NUMBER_DUPLICATE_BONUS_NUMBER;
import static lotto.utils.LottoNumberRange.MAX_LOTTO_NUMBER;
import static lotto.utils.LottoNumberRange.MIN_LOTTO_NUMBER;

import java.util.List;

public class WinningLottoNumbers {
    private final List<Integer> winningNumbers;
    private final int bonusNumber;

    public WinningLottoNumbers(List<Integer> winningNumbers, int bonusNumber) {
        validateBonusNumberRange(bonusNumber);
        validateWinningNumbersRange(winningNumbers);
        validateWinningAndBonusDuplicate(winningNumbers, bonusNumber);
        this.winningNumbers = winningNumbers;
        this.bonusNumber = bonusNumber;
    }

    private void validateBonusNumberRange(int bonusNumber) {
        if (bonusNumber < MIN_LOTTO_NUMBER.getNumber() || bonusNumber > MAX_LOTTO_NUMBER.getNumber()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
        }
    }

    private void validateWinningNumbersRange(List<Integer> winningNumbers) {
        winningNumbers.forEach(number -> {
            if (number < MIN_LOTTO_NUMBER.getNumber() || number > MAX_LOTTO_NUMBER.getNumber()) {
                throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
            }
        });
    }

    private void validateWinningAndBonusDuplicate(List<Integer> winningNumbers, int bonusNumber) {
        winningNumbers.forEach(number -> {
            if (number == bonusNumber) {
                throw new IllegalArgumentException(WINNING_NUMBER_DUPLICATE_BONUS_NUMBER.getMessage());
            }
        });
    }

    public List<Integer> getWinningNumbers() {
        return winningNumbers;
    }

    public int getBonusNumber() {
        return bonusNumber;
    }


}
