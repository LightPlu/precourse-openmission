package lotto.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import lotto.presentation.controller.LottoController;
import lotto.presentation.controller.MenuCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuCommandTest {

    @Test
    @DisplayName("from()은 입력 숫자에 맞는 MenuCommand를 반환한다")
    void fromValidNumber() {
        assertThat(MenuCommand.from(1)).isEqualTo(MenuCommand.BUY_TICKETS);
        assertThat(MenuCommand.from(2)).isEqualTo(MenuCommand.REGISTER_WINNING);
        assertThat(MenuCommand.from(3)).isEqualTo(MenuCommand.CLOSE_ROUND);
        assertThat(MenuCommand.from(4)).isEqualTo(MenuCommand.SHOW_RESULT);
        assertThat(MenuCommand.from(0)).isEqualTo(MenuCommand.EXIT);
    }

    @Test
    @DisplayName("from()은 잘못된 숫자 입력 시 예외를 던진다")
    void fromInvalidNumber() {
        assertThatThrownBy(() -> MenuCommand.from(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 입력");
    }

    @Test
    @DisplayName("BUY_TICKETS는 controller.buyTickets()를 실행한다")
    void executeBuyTickets() {
        LottoController controller = mock(LottoController.class);

        MenuCommand.BUY_TICKETS.execute(controller);

        verify(controller, times(1)).buyTickets();
    }

    @Test
    @DisplayName("REGISTER_WINNING는 controller.registerWinningNumbers()를 실행한다")
    void executeRegisterWinning() {
        LottoController controller = mock(LottoController.class);

        MenuCommand.REGISTER_WINNING.execute(controller);

        verify(controller, times(1)).registerWinningNumbers();
    }

    @Test
    @DisplayName("CLOSE_ROUND는 controller.closeRound()를 실행한다")
    void executeCloseRound() {
        LottoController controller = mock(LottoController.class);

        MenuCommand.CLOSE_ROUND.execute(controller);

        verify(controller, times(1)).closeRound();
    }

    @Test
    @DisplayName("SHOW_RESULT는 controller.showRoundResult()를 실행한다")
    void executeShowResult() {
        LottoController controller = mock(LottoController.class);

        MenuCommand.SHOW_RESULT.execute(controller);

        verify(controller, times(1)).showRoundResult();
    }

    @Test
    @DisplayName("EXIT는 controller.stop()을 실행한다")
    void executeExit() {
        LottoController controller = mock(LottoController.class);

        MenuCommand.EXIT.execute(controller);

        verify(controller, times(1)).stop();
    }
}
