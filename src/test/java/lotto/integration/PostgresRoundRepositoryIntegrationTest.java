package lotto.integration;

import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.service.LottoCompareService;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.vo.Rank;
import lotto.infrastructure.db.DBConnectionManager;
import lotto.infrastructure.repository.PostgresRoundRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostgresRoundRepositoryIntegrationTest {

    private PostgreSQLContainer<?> postgres;
    private RoundRepository roundRepository;

    @BeforeAll
    void setup() {
        postgres = new PostgreSQLContainer<>("postgres:16")
                .withInitScript("init.sql")
                .withDatabaseName("lotto")
                .withUsername("test")
                .withPassword("test");

        postgres.start();

        // 테스트용 DB 커넥션 정보 삽입
        DBConnectionManager.override(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        roundRepository = new PostgresRoundRepository();

    }

    @AfterAll
    void cleanup() {
        postgres.stop();
    }

    @Test
    void full_flow_round_ticket_winning_result() {

        // 1) Round 저장
        Round r = roundRepository.saveRound(Round.of(1));
        int roundId = r.getId();

        // 2) Ticket 저장
        Lotto lotto = Lotto.of(List.of(
                LottoNumber.of(1),
                LottoNumber.of(2),
                LottoNumber.of(3),
                LottoNumber.of(4),
                LottoNumber.of(5),
                LottoNumber.of(6)
        ));

        LottoTicket ticket = LottoTicket.of(roundId, List.of(lotto));
        roundRepository.saveTickets(List.of(ticket));

        // 3) Winning Number 저장
        WinningLottoNumbers winning = WinningLottoNumbers.of(
                0,
                List.of(
                        LottoNumber.of(1),
                        LottoNumber.of(2),
                        LottoNumber.of(3),
                        LottoNumber.of(7),
                        LottoNumber.of(8),
                        LottoNumber.of(9)
                ),
                LottoNumber.of(10),
                roundId
        );

        roundRepository.saveWinningLottoNumbers(winning);

        // 4) 결과 계산 후 저장
        LottoCompareService compareService = new LottoCompareService();
        Map<Rank, Integer> counts =
                compareService.compareTicket(ticket, winning);

        RoundResult result = RoundResult.of(0, roundId, counts);
        roundRepository.saveRoundResult(result);

        // 5) DB에서 RoundResult 다시 조회
        RoundResult loaded = roundRepository
                .findRoundResultByRoundId(roundId)
                .orElseThrow();

        Assertions.assertThat(loaded.getRoundId()).isEqualTo(roundId);
        Assertions.assertThat(loaded.getRankResults()).isEqualTo(counts);
    }
}
