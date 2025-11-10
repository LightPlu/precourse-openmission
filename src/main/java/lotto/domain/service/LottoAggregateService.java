package lotto.domain.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.CountResult;
import lotto.domain.vo.Rank;

public class LottoAggregateService {

    public LottoResult aggregateLottoResult(List<CountResult> countResults) {
        Map<Rank, Integer> resultMap = new EnumMap<>(Rank.class);

        countResults.forEach(countResult -> {

            Rank rank = Rank.valueOf(countResult.getNumberMatchCount(), countResult.getBonusNumberMatchCount());

            resultMap.merge(rank, 1, Integer::sum);
        });

        return new LottoResult(resultMap);
    }
}
