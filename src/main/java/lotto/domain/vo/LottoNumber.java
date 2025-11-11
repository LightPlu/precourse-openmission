package lotto.domain.vo;

import static lotto.domain.NumberConstants.LOTTO_END_NUMBER;
import static lotto.domain.NumberConstants.LOTTO_START_NUMBER;

public class LottoNumber {
    private final int value;

    private LottoNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < LOTTO_START_NUMBER.getValue() || value > LOTTO_END_NUMBER.getValue()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1~45 사이여야 합니다.");
        }
    }

    public static LottoNumber of(int value) {
        return new LottoNumber(value); // 정적 팩토리 메서드, 가독성을 향상시켜줌
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof LottoNumber && this.value == ((LottoNumber) o).value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}

