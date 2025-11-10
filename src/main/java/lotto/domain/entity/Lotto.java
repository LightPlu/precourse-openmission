package lotto.domain.entity;

import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_IS_NOT_DUPLICATE;
import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.utils.LottoNumberRange.MAX_LOTTO_NUMBER;
import static lotto.utils.LottoNumberRange.MIN_LOTTO_NUMBER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        validateLottoNumbersRange(numbers);
        validateDuplicate(numbers);
        this.numbers = getSortedNumbers(numbers);
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
    }

    private void validateLottoNumbersRange(List<Integer> numbers) {
        numbers.forEach(number -> {
            if (number < MIN_LOTTO_NUMBER.getNumber() || number > MAX_LOTTO_NUMBER.getNumber()) {
                throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
            }
        });
    }

    private void validateDuplicate(List<Integer> numbers) {
        Set<Integer> unique = new HashSet<>(numbers);
        if (unique.size() != numbers.size()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_IS_NOT_DUPLICATE.getMessage());
        }
    }

    private List<Integer> getSortedNumbers(List<Integer> numbers) {
        return numbers.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

}
