package lotto.domain.service;

import java.util.List;
import lotto.domain.vo.Lotto;
import lotto.domain.vo.CountResult;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.vo.LottoNumber;

public class LottoCompareService {

    public CountResult compareNumber(Lotto lotto, WinningLottoNumbers winningLottoNumbers) {
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
