package lotto.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LottoTest {

    private LottoNumber n(int v) {
        return LottoNumber.of(v);
    }

    @Test
    @DisplayName("정상적으로 Lotto 객체 생성")
    void createLottoSuccess() {
        List<LottoNumber> nums = List.of(
                n(1), n(3), n(5), n(10), n(20), n(30)
        );

        Lotto lotto = Lotto.of(nums);

        assertEquals(6, lotto.getNumbers().size());
        assertEquals(List.of(n(1), n(3), n(5), n(10), n(20), n(30)),
                lotto.getNumbers());
    }

    @Test
    @DisplayName("6개가 아닌 숫자 개수로 생성 시 예외 발생")
    void lottoMustHaveExactly6Numbers() {
        List<LottoNumber> nums = List.of(
                n(1), n(3), n(5), n(10), n(20)
        );

        assertThrows(IllegalArgumentException.class,
                () -> Lotto.of(nums));
    }

    @Test
    @DisplayName("중복 숫자가 존재하면 예외 발생")
    void lottoNumbersMustNotBeDuplicate() {
        List<LottoNumber> nums = List.of(
                n(1), n(3), n(5), n(5), n(20), n(30) // 5 중복
        );

        assertThrows(IllegalArgumentException.class,
                () -> Lotto.of(nums));
    }

    @Test
    @DisplayName("Lotto는 생성 시 자동으로 숫자를 오름차순 정렬한다")
    void lottoIsSorted() {
        List<LottoNumber> nums = List.of(
                n(30), n(1), n(20), n(5), n(3), n(10)
        );

        Lotto lotto = Lotto.of(nums);

        assertEquals(
                List.of(n(1), n(3), n(5), n(10), n(20), n(30)),
                lotto.getNumbers()
        );
    }
}
