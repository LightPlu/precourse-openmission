package lotto.domain.vo;

public class LottoNumber {
    private final int value;
    private static final int MIN = 1;
    private static final int MAX = 45;

    private LottoNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1~45 사이여야 합니다.");
        }
    }

    public static LottoNumber of(int value) {
        return new LottoNumber(value);
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

