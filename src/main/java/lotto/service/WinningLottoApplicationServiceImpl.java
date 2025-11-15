package lotto.service;

import java.util.List;
import lotto.domain.entity.Round;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.repository.RoundRepository;
import lotto.domain.vo.LottoNumber;

public class WinningLottoApplicationServiceImpl implements WinningLottoApplicationService {

    private final RoundRepository roundRepository;

    public WinningLottoApplicationServiceImpl(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Override
    public void saveWinningNumbers(List<Integer> numbers, int bonus) {

        // 1) 회차 조회
        Round round = roundRepository.findLatestRound()
                .orElseThrow(() -> new IllegalStateException("해당 회차가 존재하지 않습니다."));

        int roundId = round.getId();

        // 2) 이미 당첨 번호가 존재하는지 검사
        if (roundRepository.findWinningLottoNumbersByRoundId(roundId).isPresent()) {
            throw new IllegalStateException("이미 당첨 번호가 등록된 회차입니다.");
        }

        // 3) LottoNumber, Lotto 도메인 방식으로 변환
        List<LottoNumber> winningNumbers = numbers.stream()
                .map(LottoNumber::of)
                .toList();

        LottoNumber bonusNumber = LottoNumber.of(bonus);

        // 4) 도메인 엔티티 생성 (id = 0)
        WinningLottoNumbers winning = WinningLottoNumbers.of(
                0,
                winningNumbers,
                bonusNumber,
                roundId
        );

        // 5) DB 저장
        roundRepository.saveWinningLottoNumbers(winning);
    }

    @Override
    public WinningLottoNumbers getWinningNumbers(int roundNumber) {

        return roundRepository.findWinningLottoNumbersByRoundId(roundNumber)
                .orElseThrow(() -> new IllegalStateException("당첨번호가 등록되지 않았습니다."));
    }
}
