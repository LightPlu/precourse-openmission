package lotto.domain.vo;

import static lotto.domain.DomainErrorMessage.LOTTO_NUMBER_OUT_OF_BOUNDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LottoNumberTest {

    @Test
    @DisplayName("정상 범위(1~45) 값은 LottoNumber 생성 성공")
    void validNumber() {
        LottoNumber num = LottoNumber.of(10);

        assertThat(num.getValue()).isEqualTo(10);
    }

    @Test
    @DisplayName("0 이하의 수는 예외 발생")
    void numberTooSmall() {
        assertThatThrownBy(() -> LottoNumber.of(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @Test
    @DisplayName("45 초과의 수는 예외 발생")
    void numberTooLarge() {
        assertThatThrownBy(() -> LottoNumber.of(50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_NUMBER_OUT_OF_BOUNDS.getMessage());
    }

    @Test
    @DisplayName("동일한 숫자는 equals() 비교 시 true")
    void equalsTest() {
        LottoNumber n1 = LottoNumber.of(7);
        LottoNumber n2 = LottoNumber.of(7);

        assertThat(n1).isEqualTo(n2);
        assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
    }

    @Test
    @DisplayName("서로 다른 숫자는 equals() 비교 시 false")
    void equalsFailTest() {
        LottoNumber n1 = LottoNumber.of(7);
        LottoNumber n2 = LottoNumber.of(8);

        assertThat(n1).isNotEqualTo(n2);
    }

    @Test
    @DisplayName("Comparable 구현: 작은 수 < 큰 수")
    void compareToTest() {
        LottoNumber small = LottoNumber.of(3);
        LottoNumber big = LottoNumber.of(10);

        assertThat(small.compareTo(big)).isLessThan(0);
        assertThat(big.compareTo(small)).isGreaterThan(0);
    }

    @Test
    @DisplayName("Comparable 정렬 테스트")
    void sortingTest() {
        LottoNumber n3 = LottoNumber.of(30);
        LottoNumber n1 = LottoNumber.of(5);
        LottoNumber n2 = LottoNumber.of(20);

        // 정렬
        var sorted = java.util.List.of(n3, n1, n2)
                .stream()
                .sorted()
                .toList();

        assertThat(sorted).containsExactly(
                LottoNumber.of(5),
                LottoNumber.of(20),
                LottoNumber.of(30)
        );
    }
}
