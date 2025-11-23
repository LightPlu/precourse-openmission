package lotto.infrastructure.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lotto.domain.lottoTicket.entity.LottoTicket;
import lotto.domain.lottoTicket.repository.LottoTicketRepository;
import lotto.domain.lottoTicket.vo.Lotto;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.infrastructure.db.DBConnectionManager;

public class PostgresLottoTicketRepository implements LottoTicketRepository {

    @Override
    public void saveTickets(List<LottoTicket> tickets) {

        String sql = "INSERT INTO lotto_ticket (round_id, numbers) VALUES (?, ?)";

        int roundId = tickets.getFirst().getRoundId(); // 모든 ticket은 같은 round

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // 트랜잭션 시작

            for (LottoTicket ticket : tickets) {
                pstmt.setInt(1, ticket.getRoundId());
                pstmt.setString(2, ticket.numbersAsCsv());
                pstmt.addBatch();  // batch에 추가
            }

            pstmt.executeBatch();  // 🔥 한 번에 다 INSERT됨
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("saveAll 실행 실패", e);
        }
    }

    private List<Lotto> parseLottos(String text) {

        return Arrays.stream(text.split(";"))
                .map(line -> {
                    List<LottoNumber> nums =
                            Arrays.stream(line.split(","))
                                    .map(s -> LottoNumber.of(Integer.parseInt(s.trim())))
                                    .toList();

                    return Lotto.of(nums);
                })
                .toList();
    }

    @Override
    public List<LottoTicket> findTicketsByRoundId(int roundId) {
        String sql = "SELECT id, numbers FROM lotto_ticket WHERE round_id = ?";
        List<LottoTicket> tickets = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roundId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String numbersText = rs.getString("numbers");

                // TEXT → List<Lotto> 복원
                List<Lotto> lottos = parseLottos(numbersText);

                // 엔티티 생성
                LottoTicket ticket = LottoTicket.of(roundId, lottos);
                tickets.add(ticket);
            }

        } catch (SQLException e) {
            throw new RuntimeException("findTicketsByRoundId 실패", e);
        }

        return tickets;
    }
}
