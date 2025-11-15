package lotto.service;

import java.util.List;
import lotto.domain.entity.WinningLottoNumbers;

public interface WinningLottoApplicationService {

    // 1) 회차에 대한 당첨 번호 등록
    void saveWinningNumbers(List<Integer> numbers, int bonus);

    // 2) 회차 당첨 번호 조회
    WinningLottoNumbers getWinningNumbers(int roundNumber);

}
