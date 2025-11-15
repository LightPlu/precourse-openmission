package lotto;

import lotto.config.ApplicationConfig;
import lotto.controller.LottoController;

public class Application {
    public static void main(String[] args) {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        LottoController lottoController = applicationConfig.lottoController();

        lottoController.run();
    }
}
