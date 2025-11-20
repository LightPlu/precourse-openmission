package lotto.presentation.controller;

import java.util.List;
import java.util.Map;
import lotto.application.RoundApplicationService;
import lotto.application.RoundResultApplicationService;
import lotto.application.TicketApplicationService;
import lotto.application.WinningLottoApplicationService;
import lotto.presentation.view.UserInputView;
import lotto.presentation.view.UserOutputView;

public class LottoController {

    private final RoundApplicationService roundService;
    private final TicketApplicationService ticketService;
    private final WinningLottoApplicationService winningService;
    private final RoundResultApplicationService resultService;
    private final UserInputView userInputView = new UserInputView();
    private final UserOutputView userOutputView = new UserOutputView();
    private boolean running = true;


    public LottoController(
            RoundApplicationService roundService,
            TicketApplicationService ticketService,
            WinningLottoApplicationService winningService,
            RoundResultApplicationService resultService
    ) {
        this.roundService = roundService;
        this.ticketService = ticketService;
        this.winningService = winningService;
        this.resultService = resultService;
    }

    public void run() {
        while (running) {
            try {
                userOutputView.printMenu();
                int choice = userInputView.printChoiceMenuMessage();

                MenuCommand menuCommand = MenuCommand.from(choice);
                menuCommand.execute(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void buyTickets() {
        int money = userInputView.printPriceMessage();

        try {
            ticketService.buyTicketsAndSave(money);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void registerWinningNumbers() {
        try {
            List<Integer> winningNumbers = userInputView.printWinningNumbersMessage();
            int bonus = userInputView.printBonusNumbersMessage();

            winningService.saveWinningNumbers(winningNumbers, bonus);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void closeRound() {

        try {
            roundService.closeRoundAndStartNextRound();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showRoundResult() {
        List<Integer> roundNumbers = roundService.findAllRoundNumbers();
        userOutputView.printExistRound(roundNumbers);
        int roundNumber = userInputView.printChoiceRoundNumberMessage();
        if (roundNumber == 0) {
            return;
        }
        int round = roundService.findRoundIdByRoundNumber(roundNumber);
        try {
            aggregateMethod(round);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void aggregateMethod(int round) {
        Map<String, Integer> result = resultService.getRoundResult(round);
        Map<String, Long> prizeResult = resultService.calculateWinningPrize(round);
        long lottoNumber = resultService.calculateLottoSize(round);
        String winningNumbers = winningService.getWinningNumbers(round);
        userOutputView.printRoundResult(round, result, prizeResult, lottoNumber, winningNumbers);
    }

    public void stop() {
        this.running = false;
    }
}

