package lotto.utils;

import static lotto.utils.LottoNumberRange.MAX_LOTTO_NUMBER;
import static lotto.utils.LottoNumberRange.MIN_LOTTO_NUMBER;
import static lotto.utils.LottoNumberRange.OUTPUT_LOTTO_NUMBER_COUNT;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;

public class RandomNumberGenerator implements RandomNumberGeneratorInterface {


    @Override
    public List<Integer> numberCreates() {
        return Randoms.pickUniqueNumbersInRange(MIN_LOTTO_NUMBER.getNumber(), MAX_LOTTO_NUMBER.getNumber(),
                OUTPUT_LOTTO_NUMBER_COUNT.getNumber());
    }

}
