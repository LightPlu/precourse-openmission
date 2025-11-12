package lotto.domain.entity;

import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.exceptions.ErrorMessage.WINNING_NUMBER_DUPLICATE_BONUS_NUMBER;
import static lotto.utils.LottoNumberRange.MAX_LOTTO_NUMBER;
import static lotto.utils.LottoNumberRange.MIN_LOTTO_NUMBER;

import java.util.List;
import lotto.domain.vo.LottoNumber;

public class WinningLottoNumbers {
    private final int round;
    private final List<LottoNumber> winningNumbers;
    private final LottoNumber bonusNumber;

    public WinningLottoNumbers(int round, List<LottoNumber> winningNumbers, LottoNumber bonusNumber) {
        validateBonusNumberRange(bonusNumber);
        validateWinningNumbersRange(winningNumbers);
        validateWinningAndBonusDuplicate(winningNumbers, bonusNumber);
        this.round = round;
        this.winningNumbers = winningNumbers;
        this.bonusNumber = bonusNumber;
    }

    private void validateBonusNumberRange(LottoNumber bonusNumber) {
        if (bonusNumber.getValue() < MIN_LOTTO_NUMBER.getNumber()
                || bonusNumber.getValue() > MAX_LOTTO_NUMBER.getNumber()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
        }
    }

    private void validateWinningNumbersRange(List<LottoNumber> winningNumbers) {
        winningNumbers.forEach(number -> {
            if (number.getValue() < MIN_LOTTO_NUMBER.getNumber() || number.getValue() > MAX_LOTTO_NUMBER.getNumber()) {
                throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
            }
        });
    }

    private void validateWinningAndBonusDuplicate(List<LottoNumber> winningNumbers, LottoNumber bonusNumber) {
        winningNumbers.forEach(number -> {
            if (number.getValue() == bonusNumber.getValue()) {
                throw new IllegalArgumentException(WINNING_NUMBER_DUPLICATE_BONUS_NUMBER.getMessage());
            }
        });
    }

    public List<LottoNumber> getWinningNumbers() {
        return winningNumbers;
    }

    public LottoNumber getBonusNumber() {
        return bonusNumber;
    }

}
