package lotto.view;

import java.util.List;
import java.util.Map;

public class UserOutputView {

    public void printMenu() {
        System.out.println("\n===== 로또 시스템 =====");
        System.out.println("1. 로또 구매");
        System.out.println("2. 당첨번호 등록");
        System.out.println("3. 회차 종료");
        System.out.println("4. 회차 결과 조회");
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


    public void printRoundResult(int roundNumber, Map<String, Integer> results, Map<String, Long> prizeResult,
                                 long lottoNumber, String winningNumbers) {
        System.out.println("=== " + roundNumber + "회차 결과 ===");
        System.out.println("이번 회차 당첨번호");
        System.out.println(winningNumbers);
        System.out.println("이번 회차에 구매된 로또 갯수 : " + lottoNumber + "\n");

        results.forEach((rankName, count) -> {
            System.out.println(rankName + " : " + count + "개" + " | " + prizeResult.get(rankName) + "원");
        });
        System.out.println();
        System.out.println("당첨금 합산 : " + prizeResult.get("TOTAL"));
        System.out.println("=============================");
    }

}
