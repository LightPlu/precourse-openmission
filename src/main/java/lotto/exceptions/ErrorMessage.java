package lotto.exceptions;

public enum ErrorMessage {
    LOTTO_NUMBER_OUT_OF_BOUNDS("[ERROR] 로또 번호 범위 내의 숫자(1~45)를 입력해주세요."),
    WINNING_NUMBER_DUPLICATE_BONUS_NUMBER("[ERROR] 로또 당첨 번호와 보너스 번호가 동일합니다."),
    CASH_IS_NOT_DIVISIBLE("[ERROR] 구입 금액을 1,000원 단위로 입력해주세요."),
    CASH_IS_NOT_POSITIVE("[ERROR] 구입 금액을 0원보다 크게 입력해주세요."),
    LOTTO_NUMBER_IS_NOT_DUPLICATE("[ERROR] 로또 번호는 중복될 수 없습니다."),
    INPUT_VALUE_IS_INVALID("[ERROR] 입력값이 유효하지 않습니다."),
    CASH_RANGE_OUT("[ERROR] 구입 금액은 10만원을 넘을 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
