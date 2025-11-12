package lotto.infrastructure;

import java.util.ArrayList;
import java.util.List;
import lotto.domain.vo.Lotto;
import lotto.domain.repository.LottoRepository;

public class InMemoryLottoRepository implements LottoRepository {

    private final List<Lotto> storage = new ArrayList<>();

    @Override
    public void saveAll(List<Lotto> lottos) {
        storage.addAll(lottos);
    }

    @Override
    public List<Lotto> findAll() {
        return new ArrayList<>(storage);
    }
}

