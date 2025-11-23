package lotto.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.round.vo.CountResult;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.vo.Rank;

public class LottoCompareService {

    public Map<Rank, Integer> compareTicket(LottoTicket lottoTicket, WinningLottoNumbers winningLottoNumbers) {
        Map<Rank, Integer> rankResults = new HashMap<>();
        for (Rank rank : Rank.values()) {
            rankResults.put(rank, 0);
        }
        List<Lotto> lottos = lottoTicket.getLottos();
        lottos.forEach(lotto -> {
            CountResult result = compareNumber(lotto, winningLottoNumbers);
            Rank rank = Rank.valueOf(result.getNumberMatchCount(), result.getBonusNumberMatchCount());

            rankResults.put(rank, rankResults.getOrDefault(rank, 0) + 1);
        });
        return rankResults;
    }

    private CountResult compareNumber(Lotto lotto, WinningLottoNumbers winningLottoNumbers) {
        List<LottoNumber> lottoNumbers = lotto.getNumbers();
        List<LottoNumber> winningNumbers = winningLottoNumbers.getWinningNumbers();
        LottoNumber bonusNumber = winningLottoNumbers.getBonusNumber();

        int numberMatchCount = (int) lottoNumbers.stream()
                .filter(winningNumbers::contains)
                .count();

        if (lottoNumbers.contains(bonusNumber)) {
            return new CountResult(numberMatchCount, true);
        }

        return new CountResult(numberMatchCount, false);
    }


}
