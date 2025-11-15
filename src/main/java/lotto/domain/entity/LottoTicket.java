package lotto.domain.entity;

import java.util.List;
import java.util.stream.Collectors;
import lotto.domain.vo.Lotto;

public class LottoTicket {
    private final int id;
    private final int roundId;
    private final List<Lotto> lottos;
    private static final int LOTTO_MAXIMUM_PURCHASE = 5;

    private LottoTicket(int id, int roundId, List<Lotto> lottos) {
        validateLottoPurchaseRange(lottos);
        this.id = id;
        this.lottos = lottos;
        this.roundId = roundId;
    }

    public static LottoTicket of(int roundId, List<Lotto> lottos) {
        return new LottoTicket(0, roundId, lottos);
    }

    private void validateLottoPurchaseRange(List<Lotto> lottos) {
        if (lottos.isEmpty() || lottos.size() > LOTTO_MAXIMUM_PURCHASE) {
            throw new IllegalArgumentException("[ERROR] 로또는 1장당 1게임~5게임 사이로 구매되어야 합니다.");
        }
    }

    public List<Lotto> getLottos() {
        return List.copyOf(lottos);
    }

    public int getRoundId() {
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
