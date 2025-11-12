package lotto.domain.service;

import static lotto.domain.NumberConstants.LOTTO_END_NUMBER;
import static lotto.domain.NumberConstants.LOTTO_NUMBER_SIZE;
import static lotto.domain.NumberConstants.LOTTO_START_NUMBER;

import java.util.List;
import lotto.domain.vo.LottoNumber;

public class LottoNumberGenerator {

    private final RandomNumberGenerator randomGenerator;

    public LottoNumberGenerator(RandomNumberGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public List<LottoNumber> createNumbers() {
        return randomGenerator.createNumbers(LOTTO_START_NUMBER.getValue(), LOTTO_END_NUMBER.getValue(),
                        LOTTO_NUMBER_SIZE.getValue())
                .stream()
                .map(LottoNumber::of)
                .toList();
    }

}
