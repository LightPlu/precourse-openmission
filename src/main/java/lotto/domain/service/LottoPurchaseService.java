package lotto.domain.service;

import static lotto.domain.NumberConstants.LOTTO_GAME_MAX_PURCHASE;
import static lotto.domain.NumberConstants.ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.entity.LottoTicket;
import lotto.domain.vo.Cash;
import lotto.domain.vo.Lotto;

public class LottoPurchaseService {

    private final LottoNumberGenerator lottoNumberGenerator;

    public LottoPurchaseService(LottoNumberGenerator randomNumberGenerator) {
        this.lottoNumberGenerator = randomNumberGenerator;
    }

    public List<LottoTicket> purchase(Cash cash) {
        List<LottoTicket> lottoTickets = new ArrayList<>();

        IntStream.range(ZERO.getValue(), cash.getFullGameCount())
                .mapToObj(i -> createLottoTicket(LOTTO_GAME_MAX_PURCHASE.getValue()))
                .forEach(lottoTickets::add);

        if (cash.getRemainingGameCount() > ZERO.getValue()) {
            lottoTickets.add(createLottoTicket(cash.getRemainingGameCount()));
        }

        return lottoTickets;
    }

    private LottoTicket createLottoTicket(int gameCount) {
        List<Lotto> lottos = IntStream.range(ZERO.getValue(), gameCount)
                .mapToObj(i -> new Lotto(lottoNumberGenerator.createNumbers()))
                .toList();
        return new LottoTicket(lottos);
    }

}
