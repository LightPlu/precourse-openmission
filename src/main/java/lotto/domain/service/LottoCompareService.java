package lotto.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lotto.domain.entity.LottoTicket;
import lotto.domain.vo.Lotto;
import lotto.domain.vo.CountResult;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.vo.LottoNumber;
import lotto.domain.vo.PrizeDetail;
import lotto.domain.vo.Rank;

public class LottoCompareService {

    public List<PrizeDetail> compareTicket(LottoTicket lottoTicket, WinningLottoNumbers winningLottoNumbers) {
        Map<Rank, Integer> rankResults = countingResults(lottoTicket,  winningLottoNumbers);

        return rankResults.entrySet().stream()
                .map(entry -> PrizeDetail.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Map<Rank, Integer> countingResults(LottoTicket lottoTicket, WinningLottoNumbers winningLottoNumbers) {
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
