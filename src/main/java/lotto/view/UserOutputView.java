package lotto.view;

import java.util.List;
import lotto.domain.entity.RoundResult;

public class UserOutputView {

    public void printMenu() {
        System.out.println("\n===== 로또 시스템 =====");
        System.out.println("1. 새 회차 시작");
        System.out.println("2. 로또 구매");
        System.out.println("3. 당첨번호 등록");
        System.out.println("4. 회차 종료");
        System.out.println("5. 회차 결과 조회");
        System.out.println("0. 종료");
        System.out.println("======================");
    }

    public void printExistRound(List<Integer> roundNumbers) {

        System.out.println("\n===== 현재 존재하는 회차 목록 =====");
        if (roundNumbers.isEmpty()) {
            System.out.println("▶ 등록된 회차가 없습니다.");
            System.out.println("=================================\n");
            return;
        }

        for (Integer num : roundNumbers) {
            System.out.println("▶ " + num + "회차");
        }
        System.out.println("=================================\n");
    }


    public void printRoundResult(RoundResult r) {
        System.out.println("=== " + r.getRoundId() + "회차 결과 ===");

        r.getRankResults().forEach((rank, count) -> {
            System.out.println(rank + ": " + count + "개" + " | 등수별 총 금액 : " + rank.getPrize() * count);
        });

        System.out.println("=============================");
    }

}
