package lotto.service;

import java.util.List;
import lotto.domain.entity.LottoResult;
import lotto.domain.vo.Cash;

public interface LottoService {

    int buyLottoAndSave(int money);

    LottoResult compareLotto(List<Integer> winningNumbers, int bonusNumber);

    double totalWinningsMoney(Cash cash, LottoResult lottoResult);

    String printLottoPurchaseHistory();

    List<Integer> printLottoResultStatistics(List<Integer> winningNumbers, int bonusNumber);

}
