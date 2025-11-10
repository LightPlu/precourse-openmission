package lotto.controller;

import java.util.List;
import lotto.domain.entity.Lotto;
import lotto.domain.vo.WinningLottoNumbers;
import lotto.service.LottoServiceImpl;
import lotto.view.UserInputView;
import lotto.view.UserOutputView;

public class LottoController {

    UserOutputView userOutputView = new UserOutputView();
    UserInputView userInputView = new UserInputView();
    LottoServiceImpl lottoService = new LottoServiceImpl();

    public void run() {
        int price = getPriceWithRetry();
        int count = buyLottoWithRetry(price);

        userOutputView.printLottoCountMessage(count);
        String lottoHistory = lottoService.printLottoPurchaseHistory();
        userOutputView.printLottoHistory(lottoHistory);

        List<Integer> winningNumbers = getWinningNumbersWithRetry();

        int bonusNumber = getBonusNumberWithRetry(winningNumbers);

        double earningRate = lottoService.compareLottoAndAggregate(price, winningNumbers, bonusNumber);
        List<Integer> winningStats = lottoService.printLottoResultStatistics(winningNumbers, bonusNumber);
        userOutputView.printTotalStatics(winningStats, earningRate);
    }

    private int getPriceWithRetry() {
        while (true) {
            try {
                return userInputView.printPriceMessage();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int buyLottoWithRetry(int price) {
        while (true) {
            try {
                return lottoService.buyLottoAndSave(price);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                price = getPriceWithRetry();
            }
        }
    }

    private List<Integer> getWinningNumbersWithRetry() {
        while (true) {
            try {
                List<Integer> winningNumbers = userInputView.printWinningNumbersMessage();
                new Lotto(winningNumbers);
                return winningNumbers;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int getBonusNumberWithRetry(List<Integer> winningNumbers) {
        while (true) {
            try {
                int bonusNumber = userInputView.printBonusNumbersMessage();
                new WinningLottoNumbers(winningNumbers, bonusNumber);
                return bonusNumber;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
