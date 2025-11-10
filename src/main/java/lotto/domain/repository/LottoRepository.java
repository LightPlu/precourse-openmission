package lotto.domain.repository;

import java.util.List;
import lotto.domain.entity.Lotto;

public interface LottoRepository {

    void saveAll(List<Lotto> lottos);

    List<Lotto> findAll();

}

