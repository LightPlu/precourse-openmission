package lotto.controller;

import static lotto.controller.ApplicationErrorMessage.INPUT_VALUE_IS_INVALID;

import java.util.List;
import java.util.Map;
import lotto.service.RoundApplicationService;
import lotto.service.RoundResultApplicationService;
import lotto.service.TicketApplicationService;
import lotto.service.WinningLottoApplicationService;
import lotto.view.UserInputView;
import lotto.view.UserOutputView;

public class LottoController {

    private final RoundApplicationService roundService;
    private final TicketApplicationService ticketService;
    private final WinningLottoApplicationService winningService;
    private final RoundResultApplicationService resultService;
    private final UserInputView userInputView = new UserInputView();
    private final UserOutputView userOutputView = new UserOutputView();


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
        while (true) {
            try{
                userOutputView.printMenu();
                int choice = userInputView.printChoiceMenuMessage();

                switch (choice) {
                    case 1 -> startNewRound();
                    case 2 -> buyTickets();
                    case 3 -> registerWinningNumbers();
                    case 4 -> closeRound();
                    case 5 -> showRoundResult();
                    case 0 -> {
                        System.out.println("프로그램 종료");
                        return;
                    }
                    default -> System.out.println(INPUT_VALUE_IS_INVALID.getMessage());
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    private void startNewRound() {
        roundService.startNewRound();
    }

    private void buyTickets() {
        int money = userInputView.printPriceMessage();

        try {
            ticketService.buyTicketsAndSave(money);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void registerWinningNumbers() {
        while(true) {
            try {
                List<Integer> winningNumbers = userInputView.printWinningNumbersMessage();
                int bonus = userInputView.printBonusNumbersMessage();

                winningService.saveWinningNumbers(winningNumbers, bonus);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void closeRound() {

        try {
            roundService.closeRoundAndStartNextRound();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showRoundResult() {
        List<Integer> roundNumbers = roundService.findAllRoundNumbers();
        userOutputView.printExistRound(roundNumbers);
        int round = userInputView.printChoiceRoundNumberMessage();

        try {
            Map<String, Integer> result = resultService.getRoundResult(round);
            Map<String, Long> prizeResult = resultService.calculateWinningPrize(round);
            long lottoNumber = resultService.calculateLottoSize(round);
            String winningNumbers = winningService.getWinningNumbers(round);
            userOutputView.printRoundResult(round, result, prizeResult, lottoNumber, winningNumbers);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

