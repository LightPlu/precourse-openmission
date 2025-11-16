package lotto.controller;

import java.util.Arrays;

public enum MenuCommand {

    BUY_TICKETS(1) {
        @Override
        public void execute(LottoController controller) {
            controller.buyTickets();
        }
    },
    REGISTER_WINNING(2) {
        @Override
        public void execute(LottoController controller) {
            controller.registerWinningNumbers();
        }
    },
    CLOSE_ROUND(3) {
        @Override
        public void execute(LottoController controller) {
            controller.closeRound();
        }
    },
    SHOW_RESULT(4) {
        @Override
        public void execute(LottoController controller) {
            controller.showRoundResult();
        }
    },
    EXIT(0) {
        @Override
        public void execute(LottoController controller) {
            System.out.println("프로그램 종료");
            controller.stop();
        }
    };

    private final int number;

    MenuCommand(int number) {
        this.number = number;
    }

    public abstract void execute(LottoController controller);

    public static MenuCommand from(int number) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.number == number)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
    }
}
