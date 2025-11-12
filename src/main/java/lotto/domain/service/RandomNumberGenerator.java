package lotto.domain.service;

import java.util.List;

public interface RandomNumberGenerator {

    List<Integer> createNumbers(int min, int max, int count);
}
