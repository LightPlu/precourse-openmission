package lotto.domain.vo;

import static lotto.domain.NumberConstants.LOTTO_END_NUMBER;
import static lotto.domain.NumberConstants.LOTTO_START_NUMBER;
import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_IS_NOT_DUPLICATE;
import static lotto.exceptions.ErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lotto {
    private final List<LottoNumber> numbers;

    private Lotto(List<LottoNumber> numbers) {
        validate(numbers);
        validateLottoNumbersRange(numbers);
        validateDuplicate(numbers);
        this.numbers = getSortedNumbers(numbers);
    }

    public static Lotto of(List<LottoNumber> numbers) {
        return new Lotto(numbers);
    }

    private void validate(List<LottoNumber> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
    }

    private void validateLottoNumbersRange(List<LottoNumber> numbers) {
        numbers.forEach(number -> {
            if (number.getValue() < LOTTO_START_NUMBER.getValue() || number.getValue() > LOTTO_END_NUMBER.getValue()) {
                throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
            }
        });
    }

    private void validateDuplicate(List<LottoNumber> numbers) {
        Set<LottoNumber> unique = new HashSet<>(numbers);
        if (unique.size() != numbers.size()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_IS_NOT_DUPLICATE.getMessage());
        }
    }

    private List<LottoNumber> getSortedNumbers(List<LottoNumber> numbers) {
        return numbers.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<LottoNumber> getNumbers() {
        return numbers;
    }

}
