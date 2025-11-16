package lotto.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lotto.domain.vo.LottoNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoNumberGeneratorTest {

    static class FakeRandomGenerator implements RandomNumberGenerator {
        @Override
        public List<Integer> createNumbers(int start, int end, int size) {
            return List.of(1, 2, 3, 4, 5, 6);
        }
    }

    @Test
    @DisplayName("LottoNumberGenerator는 RandomGenerator가 준 숫자를 LottoNumber로 변환한다")
    void generateLottoNumbers() {
        RandomNumberGenerator fake = new FakeRandomGenerator();
        LottoNumberGenerator generator = new LottoNumberGenerator(fake);

        List<LottoNumber> numbers = generator.createNumbers();

        assertThat(numbers)
                .hasSize(6)
                .extracting(LottoNumber::getValue)
                .containsExactly(1, 2, 3, 4, 5, 6);
    }
}
