package lotto.application;

import static lotto.application.common.ServiceErrorMessage.NOT_REGISTERED_WINNING_NUMBERS;
import static lotto.application.common.ServiceErrorMessage.NO_ROUND;
import static lotto.application.common.ServiceErrorMessage.REGISTERED_WINNING_NUMBERS;

import java.util.List;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.lottoTicket.vo.LottoNumber;

public class WinningLottoApplicationServiceImpl implements WinningLottoApplicationService {
    private final String WINNING_NUMBER_MESSAGE_PREFIX = "당첨번호 : ";
    private final String BONUS_NUMBER_MESSAGE_PREFIX = " | 보너스 번호 : ";
    private final RoundRepository roundRepository;

    public WinningLottoApplicationServiceImpl(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Override
    public void saveWinningNumbers(List<Integer> numbers, int bonus) {

        Round round = roundRepository.findLatestRound()
                .orElseThrow(() -> new IllegalStateException(NO_ROUND.getMessage()));


        if (round.getWinningLottoNumbers() != null) {
            throw new IllegalStateException(REGISTERED_WINNING_NUMBERS.getMessage());
        }

        List<LottoNumber> winningNumbers = numbers.stream()
                .map(LottoNumber::of)
                .toList();

        LottoNumber bonusNumber = LottoNumber.of(bonus);

        roundRepository.saveWinningLottoNumbers(WinningLottoNumbers.of(
                0,
                winningNumbers,
                bonusNumber,
                round.getId()
        ));
    }

    @Override
    public String getWinningNumbers(int roundId) {
        Round round = roundRepository.findByRoundId(roundId)
                .orElseThrow(() -> new IllegalStateException(NOT_REGISTERED_WINNING_NUMBERS.getMessage()));

        WinningLottoNumbers winningLottoNumbers = round.getWinningLottoNumbers();

        return WINNING_NUMBER_MESSAGE_PREFIX + winningLottoNumbers.winningNumbersAsCsv() + BONUS_NUMBER_MESSAGE_PREFIX
                + winningLottoNumbers.bonusNumberAsCsv();
    }
}
