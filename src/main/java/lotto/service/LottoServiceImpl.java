package lotto.service;

import static lotto.utils.NumberConstants.LOTTO_PRICE;

import java.util.ArrayList;
import java.util.List;
import lotto.domain.entity.Lotto;
import lotto.domain.entity.LottoResult;
import lotto.domain.service.LottoAggregateService;
import lotto.domain.service.LottoCompareService;
import lotto.domain.service.LottoPrizeCalculateService;
import lotto.domain.service.LottoPurchaseService;
import lotto.domain.vo.Cash;
import lotto.domain.vo.CountResult;
import lotto.domain.vo.WinningLottoNumbers;
import lotto.domain.repository.LottoRepository;
import lotto.infrastructure.InMemoryLottoRepository;
import lotto.utils.RandomNumberGenerator;

public class LottoServiceImpl implements LottoService {

    private final LottoPurchaseService lottoPurchaseService;
    private final LottoPrizeCalculateService lottoPrizeCalculateService;
    private final LottoCompareService lottoCompareService;
    private final LottoAggregateService lottoAggregateService;
    private final LottoRepository lottoRepository;

    public LottoServiceImpl() {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
        this.lottoPurchaseService = new LottoPurchaseService(randomNumberGenerator);
        this.lottoPrizeCalculateService = new LottoPrizeCalculateService();
        this.lottoCompareService = new LottoCompareService();
        this.lottoAggregateService = new LottoAggregateService();
        this.lottoRepository = new InMemoryLottoRepository();
    }

    @Override
    public int buyLottoAndSave(int money) {
        Cash cash = new Cash(money);
        List<Lotto> lottos = lottoPurchaseService.purchase(cash);

        lottoRepository.saveAll(lottos);
        return cash.getMoney() / LOTTO_PRICE.getValue();
    }

    public double compareLottoAndAggregate(int money, List<Integer> winningNumbers, int bonusNumber) {
        Cash cash = new Cash(money);
        LottoResult result = compareLotto(winningNumbers, bonusNumber);
        return totalWinningsMoney(cash, result);
    }

    @Override
    public LottoResult compareLotto(List<Integer> winningNumbers, int bonusNumber) {
        WinningLottoNumbers winningLottoNumbers = new WinningLottoNumbers(winningNumbers, bonusNumber);

        List<Lotto> savedLottos = lottoRepository.findAll();

        List<CountResult> countResults = new ArrayList<>();
        savedLottos.forEach(lotto -> {
            CountResult result = lottoCompareService.compareNumber(lotto, winningLottoNumbers);
            countResults.add(result);
        });

        return lottoAggregateService.aggregateLottoResult(countResults);
    }

    @Override
    public double totalWinningsMoney(Cash cash, LottoResult lottoResult) {
        return lottoPrizeCalculateService.calculateEarningRate(cash, lottoResult);
    }

    public List<Lotto> getSavedLottos() {
        return lottoRepository.findAll();
    }

    @Override
    public String printLottoPurchaseHistory() {
        List<Lotto> savedLottos = getSavedLottos();
        StringBuilder sb = new StringBuilder();
        savedLottos.forEach(lotto -> {
            sb.append(lotto.getNumbers()).append("\n");
        });
        return sb.toString().trim();
    }

    @Override
    public List<Integer> printLottoResultStatistics(List<Integer> winningNumbers, int bonusNumber) {
        LottoResult result = compareLotto(winningNumbers, bonusNumber);
        return result.getResultValuesOrdered();
    }

}
