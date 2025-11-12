package lotto.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lotto.domain.vo.Lotto;
import lotto.domain.repository.LottoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryLottoRepositoryTest {

    private LottoRepository lottoRepository;

    @BeforeEach
    void setUp() {
        lottoRepository = new InMemoryLottoRepository();
    }

    @DisplayName("로또 목록을 저장하고 조회한다")
    @Test
    void saveAndFindAll() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(7, 8, 9, 10, 11, 12))
        );

        // when
        lottoRepository.saveAll(lottos);
        List<Lotto> savedLottos = lottoRepository.findAll();

        // then
        assertThat(savedLottos).hasSize(2);
    }

}

