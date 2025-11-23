package lotto.domain.round.vo;

import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.lottoTicket.vo.LottoNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static lotto.domain.common.DomainErrorMessage.*;
import static org.assertj.core.api.Assertions.*;

class WinningLottoNumbersTest {

    private LottoNumber n(int num) {
        return LottoNumber.of(num);
    }

    @Test
    @DisplayName("정상적인 당첨 번호 + 보너스 번호 생성 성공")
    void validWinningNumbers() {
        List<LottoNumber> wins = List.of(
                n(1), n(3), n(5), n(10), n(20), n(30)
        );
        LottoNumber bonus = n(15);

        WinningLottoNumbers w = WinningLottoNumbers.of(0, wins, bonus, 1);

        assertThat(w.getWinningNumbers()).hasSize(6);
        assertThat(w.getBonusNumber().getValue()).isEqualTo(15);
        assertThat(w.getRoundId()).isEqualTo(1);
    }

    @Test
    @DisplayName("당첨 번호가 6개가 아니면 예외 발생")
    void invalidSize() {
        List<LottoNumber> wins = List.of(
                n(1), n(3), n(5), n(10), n(20) // 5개
        );
        LottoNumber bonus = n(15);

        assertThatThrownBy(() ->
                WinningLottoNumbers.of(0, wins, bonus, 1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(LOTTO_SIZE_NOT_ALLOWED.getMessage());
    }

    @Test
    @DisplayName("보너스 번호가 당첨 번호와 중복되면 예외 발생")
    void bonusDuplicateWinning() {
        List<LottoNumber> wins = List.of(
                n(1), n(3), n(5), n(10), n(20), n(30)
        );
        LottoNumber bonus = n(10); // 중복됨

        assertThatThrownBy(() ->
                WinningLottoNumbers.of(0, wins, bonus, 1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(WINNING_NUMBER_DUPLICATE_BONUS_NUMBER.getMessage());
    }

    @Test
    @DisplayName("당첨 번호 CSV 문자열 변환 테스트")
    void winningNumbersAsCsvTest() {
        List<LottoNumber> wins = List.of(
                n(1), n(3), n(5), n(10), n(20), n(30)
        );
        LottoNumber bonus = n(15);

        WinningLottoNumbers w = WinningLottoNumbers.of(0, wins, bonus, 1);

        assertThat(w.winningNumbersAsCsv()).isEqualTo("1,3,5,10,20,30");
    }

    @Test
    @DisplayName("보너스 번호 CSV 문자열 변환 테스트")
    void bonusNumberAsCsvTest() {
        List<LottoNumber> wins = List.of(
                n(1), n(3), n(5), n(10), n(20), n(30)
        );
        LottoNumber bonus = n(15);

        WinningLottoNumbers w = WinningLottoNumbers.of(0, wins, bonus, 1);

        assertThat(w.bonusNumberAsCsv()).isEqualTo("15");
    }
}
