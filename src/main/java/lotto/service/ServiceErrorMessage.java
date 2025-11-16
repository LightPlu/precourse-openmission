package lotto.service;

public enum ServiceErrorMessage {
    REGISTERED_WINNING_NUMBERS("[ERROR] 이미 당첨 번호가 등록된 회차입니다."),
    NOT_REGISTERED_WINNING_NUMBERS("[ERROR] 당첨 번호가 등록되지 않았습니다."),
    NO_TICKETS("[ERROR] 이번 회차에 발행된 티켓이 없습니다."),
    NOT_FINISHED_ROUND("[ERROR] 해당 회차가 아직 끝나지 않았습니다."),
    NO_ROUND("[ERROR] 해당 회차가 존재하지 않습니다.");

    String message;

    ServiceErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
