package lotto.domain.lottoTicket.vo;

import static lotto.domain.common.DomainErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static lotto.domain.common.NumberConstants.LOTTO_END_NUMBER;
import static lotto.domain.common.NumberConstants.LOTTO_START_NUMBER;

public class LottoNumber implements Comparable<LottoNumber> {
    private final int value;

    private LottoNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < LOTTO_START_NUMBER.getValue() || value > LOTTO_END_NUMBER.getValue()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
        }
    }

    public static LottoNumber of(int value) {
        return new LottoNumber(value); // 정적 팩토리 메서드, 가독성을 향상시켜줌
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(LottoNumber other) {
        return Integer.compare(this.value, other.value);
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

