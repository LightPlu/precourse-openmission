package lotto.domain.entity;

import java.util.List;
import java.util.stream.Collectors;
import lotto.domain.vo.Lotto;

public class LottoTicket {
    private final long id;
    private final long roundId;
    private final List<Lotto> lottos;
    private static final int LOTTO_MAXIMUM_PURCHASE = 5;

    private LottoTicket(long id, List<Lotto> lottos, long roundId) {
        validateLottoPurchaseRange(lottos);
        this.id = id;
        this.lottos = lottos;
        this.roundId = roundId;
    }

    public static LottoTicket of(long id, List<Lotto> lottos, long roundId) {
        return new LottoTicket(id, lottos, roundId);
    }

    private void validateLottoPurchaseRange(List<Lotto> lottos) {
        if (lottos.isEmpty() || lottos.size() > LOTTO_MAXIMUM_PURCHASE) {
            throw new IllegalArgumentException("[ERROR] 로또는 1장당 1게임~5게임 사이로 구매되어야 합니다.");
        }
    }

    public List<Lotto> getLottos() {
        return List.copyOf(lottos);
    }

    public long getRoundId() {
        return roundId;
    }

    public String numbersAsCsv() {
        return lottos.stream()
                .map(lotto -> lotto.getNumbers().stream()
                        .map(num -> String.valueOf(num.getValue()))
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }
}
