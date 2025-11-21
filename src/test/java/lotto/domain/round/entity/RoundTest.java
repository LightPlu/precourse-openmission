package lotto.domain.round.entity;

import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.vo.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RoundTest {

    private WinningLottoNumbers sampleWinning(int roundId) {
        return WinningLottoNumbers.of(
                1,
                List.of(
                        LottoNumber.of(1), LottoNumber.of(2), LottoNumber.of(3),
                        LottoNumber.of(4), LottoNumber.of(5), LottoNumber.of(6)
                ),
                LottoNumber.of(7),
                roundId
        );
    }

    private RoundResult sampleResult(int roundId) {
        Map<Rank, Integer> map = new EnumMap<>(Rank.class);
        map.put(Rank.FIRST, 1);
        map.put(Rank.SECOND, 2);
        map.put(Rank.THIRD, 3);
        map.put(Rank.FOURTH, 4);
        map.put(Rank.FIFTH, 5);
        map.put(Rank.MISS, 10);

        return RoundResult.of(1, roundId, map);
    }

    @Test
    @DisplayName("당첨번호는 정상적으로 등록된다")
    void registerWinningNumbers_success() {
        Round round = new Round(1, 1);
        WinningLottoNumbers winning = sampleWinning(1);

        round.registerWinningNumbers(winning);

        assertThat(round.getWinningLottoNumbers()).isEqualTo(winning);
    }

    @Test
    @DisplayName("당첨번호는 한 번만 등록할 수 있다")
    void registerWinningNumbers_duplicate_fail() {
        Round round = new Round(1, 1);
        WinningLottoNumbers winning = sampleWinning(1);
        round.registerWinningNumbers(winning);

        assertThatThrownBy(() -> round.registerWinningNumbers(winning))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 당첨 번호가 등록된 Round입니다.");
    }

    @Test
    @DisplayName("당첨번호 없이 결과를 등록하면 예외 발생")
    void registerRoundResult_withoutWinning_fail() {
        Round round = new Round(1, 1);
        RoundResult result = sampleResult(1);

        assertThatThrownBy(() -> round.registerRoundResult(result))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("당첨 번호 없이 결과를 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("결과는 정상적으로 등록된다")
    void registerRoundResult_success() {
        Round round = new Round(1, 1);
        round.registerWinningNumbers(sampleWinning(1));

        RoundResult result = sampleResult(1);
        round.registerRoundResult(result);

        assertThat(round.getRoundResult()).isEqualTo(result);
    }

    @Test
    @DisplayName("결과는 한 번만 등록할 수 있다")
    void registerRoundResult_duplicate_fail() {
        Round round = new Round(1, 1);
        round.registerWinningNumbers(sampleWinning(1));
        round.registerRoundResult(sampleResult(1));

        assertThatThrownBy(() -> round.registerRoundResult(sampleResult(1)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 결과가 등록된 Round입니다.");
    }

    @Test
    @DisplayName("결과 조회 시 아직 등록되지 않았다면 예외 발생")
    void getRoundResult_notRegistered_fail() {
        Round round = new Round(1, 1);

        assertThatThrownBy(round::getRoundResult)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("아직 종료되지 않은 회차입니다.");
    }

    @Test
    @DisplayName("동일한 roundNumber라면 두 Round는 같다")
    void equals_hashCode_success() {
        Round r1 = new Round(1, 5);
        Round r2 = new Round(2, 5); // id 다르지만 roundNumber 같음

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    @DisplayName("roundNumber가 다르면 다른 객체로 취급된다")
    void equals_hashCode_fail() {
        Round r1 = new Round(1, 5);
        Round r2 = new Round(1, 6);

        assertThat(r1).isNotEqualTo(r2);
    }
}
