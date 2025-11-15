package lotto.infrastructure;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;
import lotto.domain.service.RandomNumberGenerator;

public class RandomNumberGeneratorImpl implements RandomNumberGenerator {

    @Override
    public List<Integer> createNumbers(int min, int max, int count) {
        return Randoms.pickUniqueNumbersInRange(min, max, count);
    }

}
