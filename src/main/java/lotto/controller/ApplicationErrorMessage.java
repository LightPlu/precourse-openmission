package lotto.controller;

public enum ApplicationErrorMessage {
    INPUT_VALUE_IS_INVALID("[ERROR] 메뉴 선택 번호를 정확히 입력해주세요.");

    private final String message;

    ApplicationErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
