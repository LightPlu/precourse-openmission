package lotto.application.lottoService;

import static lotto.domain.common.NumberConstants.LOTTO_GAME_MAX_PURCHASE;
import static lotto.domain.common.NumberConstants.ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.service.LottoNumberGenerator;
import lotto.domain.vo.Cash;
import lotto.domain.lottoTicket.vo.Lotto;

public class LottoPurchaseService {

    private final LottoNumberGenerator lottoNumberGenerator;

    public LottoPurchaseService(LottoNumberGenerator randomNumberGenerator) {
        this.lottoNumberGenerator = randomNumberGenerator;
    }

    public List<List<Lotto>> purchase(Cash cash) {
        List<List<Lotto>> result = new ArrayList<>();

        IntStream.range(0, cash.getFullGameCount())
                .mapToObj(i -> createLottoTicket(LOTTO_GAME_MAX_PURCHASE.getValue()))
                .forEach(result::add);

        if (cash.getRemainingGameCount() > 0) {
            result.add(createLottoTicket(cash.getRemainingGameCount()));
        }

        return result;
    }

    private List<Lotto> createLottoTicket(int gameCount) {
        return IntStream.range(ZERO.getValue(), gameCount)
                .mapToObj(i -> Lotto.of(lottoNumberGenerator.createNumbers()))
                .toList();
    }

}
