package lotto.domain.entity;

import java.util.List;
import lotto.domain.vo.Lotto;

public class LottoTicket {
    private final long roundId;
    private final List<Lotto> lottos;
    private static final int LOTTO_MAXIMUM_PURCHASE = 5;

    private LottoTicket(List<Lotto> lottos, long roundId) {
        validateLottoPurchaseRange(lottos);
        this.lottos = lottos;
        this.roundId = roundId;
    }

    public static LottoTicket of(List<Lotto> lottos, long roundId) {
        return new LottoTicket(lottos, roundId);
    }

    private void validateLottoPurchaseRange(List<Lotto> lottos) {
        if (lottos.isEmpty() || lottos.size() > LOTTO_MAXIMUM_PURCHASE) {
            throw new IllegalArgumentException("[ERROR] 로또는 1장당 1게임~5게임 사이로 구매되어야 합니다.");
        }
    }

    public List<Lotto> getLottos() {
        return List.copyOf(lottos);
    }

}
