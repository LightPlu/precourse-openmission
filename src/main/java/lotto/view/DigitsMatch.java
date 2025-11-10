package lotto.view;

public enum DigitsMatch {
    THREE_DIGITS_MATCH("3개 일치 (5,000원) - %d개"),
    FOUR_DIGITS_MATCH("4개 일치 (50,000원) - %d개"),
    FIVE_DIGITS_MATCH("5개 일치 (1,500,000원) - %d개"),
    FIVE_DIGITS_ONE_BONUS_MATCH("5개 일치, 보너스 볼 일치 (30,000,000원) - %d개"),
    SIX_DIGITS_MATCH("6개 일치 (2,000,000,000원) - %d개"),
    EARNING_RATE("총 수익률은 %.1f%%입니다.");

    private final String message;

    DigitsMatch(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
