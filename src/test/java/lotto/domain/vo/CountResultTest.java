package lotto.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CountResultTest {

    @DisplayName("일치하는 번호 개수와 보너스 번호 일치 여부로 CountResult 객체를 생성한다")
    @Test
    void createCountResult() {
        int matchCount = 5;
        boolean bonusMatch = true;

        CountResult countResult = new CountResult(matchCount, bonusMatch);

        assertThat(countResult.getNumberMatchCount()).isEqualTo(matchCount);
        assertThat(countResult.getBonusNumberMatchCount()).isEqualTo(bonusMatch);
    }

    @DisplayName("보너스 번호가 일치하지 않는 CountResult 객체를 생성한다")
    @Test
    void createCountResultWithoutBonusMatch() {
        int matchCount = 3;
        boolean bonusMatch = false;

        CountResult countResult = new CountResult(matchCount, bonusMatch);

        assertThat(countResult.getNumberMatchCount()).isEqualTo(matchCount);
        assertThat(countResult.getBonusNumberMatchCount()).isEqualTo(bonusMatch);
    }
}

