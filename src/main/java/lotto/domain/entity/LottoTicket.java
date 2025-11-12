package lotto.domain.entity;

import java.util.List;
import lotto.domain.vo.Lotto;

public class LottoTicket {
    private final int round;
    private final int id;
    private final List<Lotto> lottos;
    private static final int LOTTO_MAXIMUM_PURCHASE = 5;

    public LottoTicket(int round, int id, List<Lotto> lottos) {
        validateLottoPurchaseRange(lottos);
        this.round = round;
        this.id = id;
        this.lottos = lottos;
    }

    private void validateLottoPurchaseRange(List<Lotto> lottos) {
        if (lottos.isEmpty() || lottos.size() > LOTTO_MAXIMUM_PURCHASE) {
            throw new IllegalArgumentException("[ERROR] 로또는 1장당 1게임~5게임 사이로 구매되어야 합니다.");
        }
    }

    public LottoTicket of(int round, int id, List<Lotto> lottos) {
        return new LottoTicket(round, id, lottos);
    }

}
