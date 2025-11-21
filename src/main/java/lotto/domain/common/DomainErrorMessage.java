package lotto.domain.common;

public enum DomainErrorMessage {
    CASH_IS_NOT_DIVISIBLE("[ERROR] 구입 금액을 1,000원 단위로 입력해주세요."),
    CASH_IS_NOT_POSITIVE("[ERROR] 구입 금액을 0원보다 크게 입력해주세요."),
    CASH_RANGE_OUT("[ERROR] 구입 금액은 10만원을 넘을 수 없습니다."),
    LOTTO_NUMBER_OUT_OF_BOUNDS("[ERROR] 로또 번호 범위 내의 숫자(1~45)를 입력해주세요."),
    WINNING_NUMBER_DUPLICATE_BONUS_NUMBER("[ERROR] 로또 당첨 번호와 보너스 번호가 동일합니다."),
    LOTTO_NUMBER_IS_NOT_DUPLICATE("[ERROR] 로또 번호는 중복될 수 없습니다."),
    LOTTO_SIZE_NOT_ALLOWED("[ERROR] 로또 번호는 6개여야 합니다."),
    WINNING_NUMBER_REGISTERED("[ERROR] 이미 당첨 번호가 등록된 회차입니다."),
    CANT_REGISTER_WITH_NO_WINNING_NUMBER("[ERROR] 당첨 번호 없이 결과를 등록할 수 없습니다."),
    RESULT_REGISTERED("[ERROR] 이미 결과가 등록된 회차입니다."),
    ROUND_IS_RUNNING("[ERROR] 아직 종료되지 않은 회차입니다.");



    private final String message;

    DomainErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
