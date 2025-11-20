package lotto.application;

import java.util.List;

public interface WinningLottoApplicationService {

    // 1) 회차에 대한 당첨 번호 등록
    void saveWinningNumbers(List<Integer> numbers, int bonus);

    // 2) 회차 당첨 번호 조회
    String getWinningNumbers(int roundNumber);

}
