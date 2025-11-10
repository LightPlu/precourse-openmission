package lotto.domain.service;

import static lotto.utils.NumberConstants.LOTTO_PRICE;

import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.entity.Lotto;
import lotto.domain.vo.Cash;
import lotto.utils.RandomNumberGenerator;

public class LottoPurchaseService {

    private final RandomNumberGenerator randomNumberGenerator;

    public LottoPurchaseService(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public List<Lotto> purchase(Cash cash) {
        int count = calculateCount(cash);

        return IntStream.range(0, count)
                .mapToObj(i -> new Lotto(randomNumberGenerator.numberCreates()))
                .toList();
    }

    private int calculateCount(Cash cash) {
        return cash.getMoney() / LOTTO_PRICE.getValue();
    }
}
