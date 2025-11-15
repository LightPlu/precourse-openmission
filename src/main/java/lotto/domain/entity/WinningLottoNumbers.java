package lotto.domain.entity;

import static lotto.domain.NumberConstants.LOTTO_START_NUMBER;
import static lotto.domain.NumberConstants.LOTTO_END_NUMBER;
import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.exceptions.ErrorMessage.WINNING_NUMBER_DUPLICATE_BONUS_NUMBER;

import java.util.List;
import java.util.stream.Collectors;
import lotto.domain.vo.LottoNumber;

public class WinningLottoNumbers {
    private final int id;
    private final int roundId;
    private final List<LottoNumber> winningNumbers;
    private final LottoNumber bonusNumber;

    private WinningLottoNumbers(int id, List<LottoNumber> winningNumbers, LottoNumber bonusNumber, int roundId) {
        validateBonusNumberRange(bonusNumber);
        validateWinningNumbersRange(winningNumbers);
        validateWinningAndBonusDuplicate(winningNumbers, bonusNumber);
        this.id = id;
        this.winningNumbers = winningNumbers;
        this.bonusNumber = bonusNumber;
        this.roundId = roundId;
    }

    public static WinningLottoNumbers of(int id, List<LottoNumber> winningNumbers, LottoNumber bonusNumber, int roundId) {
        return new WinningLottoNumbers(id, winningNumbers, bonusNumber, roundId);
    }

    private void validateBonusNumberRange(LottoNumber bonusNumber) {
        if (bonusNumber.getValue() < LOTTO_START_NUMBER.getValue()
                || bonusNumber.getValue() > LOTTO_END_NUMBER.getValue()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
        }
    }

    private void validateWinningNumbersRange(List<LottoNumber> winningNumbers) {
        winningNumbers.forEach(number -> {
            if (number.getValue() < LOTTO_START_NUMBER.getValue() || number.getValue() > LOTTO_END_NUMBER.getValue()) {
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

    public int getRoundId() {
        return roundId;
    }

    public List<LottoNumber> getWinningNumbers() {
        return winningNumbers;
    }

    public LottoNumber getBonusNumber() {
        return bonusNumber;
    }

    public String winningNumbersAsCsv() {
        return winningNumbers.stream()
                .map(n -> String.valueOf(n.getValue()))
                .collect(Collectors.joining(","));
    }


}
