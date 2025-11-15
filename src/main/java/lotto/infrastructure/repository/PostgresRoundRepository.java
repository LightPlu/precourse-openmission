package lotto.infrastructure.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lotto.domain.entity.LottoTicket;
import lotto.domain.entity.Round;
import lotto.domain.entity.RoundResult;
import lotto.domain.entity.WinningLottoNumbers;
import lotto.domain.repository.RoundRepository;
import lotto.domain.vo.Lotto;
import lotto.domain.vo.LottoNumber;
import lotto.domain.vo.Rank;
import lotto.infrastructure.db.DBConnectionManager;

public class PostgresRoundRepository implements RoundRepository {

    @Override
    public Optional<Round> findLatestRound() {
        String sql = "SELECT id, round_number FROM round ORDER BY round_number DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int id = rs.getInt("id");
                int roundNumber = rs.getInt("round_number");

                return Optional.of(new Round(id, roundNumber));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("findLatestRound() 실행 실패", e);
        }
    }

    @Override
    public Optional<Round> findByRoundNumber(int roundNumber) {
        String sql = "SELECT id, round_number FROM round WHERE round_number = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roundNumber); // ?인자 첫번째를 바인딩 해주는 역할

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt("id");
                    int number = rs.getInt("round_number");

                    return Optional.of(new Round(id, number));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("findByRoundNumber() 실행 실패", e);
        }
    }

    @Override
    public Round saveRound(Round round) {
        // INSERT SQL — RETURNING id로 생성된 PK를 받아오기
        String sql = "INSERT INTO round (round_number) VALUES (?) RETURNING id";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, round.getRoundNumber());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id");
                    return new Round(generatedId, round.getRoundNumber());
                } else {
                    throw new RuntimeException("Round INSERT 실패 — id가 반환되지 않음");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("save(Round) 실행 실패", e);
        }
    }

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

    @Override
    public void saveWinningLottoNumbers(WinningLottoNumbers winning) {
        String sql = "INSERT INTO winning_lotto_numbers (round_id, numbers, bonus_number) " +
                "VALUES (?, ?, ?) RETURNING round_id";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LottoNumber bonusNumber = winning.getBonusNumber();

            pstmt.setInt(1, winning.getRoundId());
            pstmt.setString(2, winning.winningNumbersAsCsv());
            pstmt.setInt(3, bonusNumber.getValue());

            pstmt.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException("saveWinningLottoNumbers 실패", e);
        }
    }

    private List<LottoNumber> parseWinningNumbers(String csv) {
        return Arrays.stream(csv.split(","))
                .map(s -> LottoNumber.of(Integer.parseInt(s.trim())))
                .toList();
    }


    @Override
    public Optional<WinningLottoNumbers> findWinningLottoNumbersByRoundId(int roundId) {
        String sql = "SELECT round_id, numbers, bonus_number " +
                "FROM winning_lotto_numbers WHERE round_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roundId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt("round_id");

                    List<LottoNumber> winningList =
                            parseWinningNumbers(rs.getString("numbers"));

                    LottoNumber bonus =
                            LottoNumber.of(rs.getInt("bonus_number"));

                    return Optional.of(WinningLottoNumbers.of(
                            id,
                            winningList,
                            bonus,
                            roundId
                    ));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("findWinningLottoNumbersByRoundId 실행 실패", e);
        }
    }

    @Override
    public RoundResult saveRoundResult(RoundResult result) {
        String sql = "INSERT INTO round_result (" +
                "round_id, first, second, third, fourth, fifth, miss" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Map<Rank, Integer> r = result.getRankResults();

            pstmt.setInt(1, result.getRoundId());
            pstmt.setInt(2, r.get(Rank.FIRST));
            pstmt.setInt(3, r.get(Rank.SECOND));
            pstmt.setInt(4, r.get(Rank.THIRD));
            pstmt.setInt(5, r.get(Rank.FOURTH));
            pstmt.setInt(6, r.get(Rank.FIFTH));
            pstmt.setInt(7, r.get(Rank.MISS));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id");
                    return RoundResult.of(
                            generatedId,
                            result.getRoundId(),
                            result.getRankResults()
                    );
                } else {
                    throw new RuntimeException("RoundResult 삽입 실패 – id가 반환되지 않았습니다.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("saveRoundResult 실행 실패", e);
        }
    }


    private Map<Rank, Integer> buildRankResults(ResultSet rs) throws SQLException {
        Map<Rank, Integer> map = new EnumMap<>(Rank.class);

        map.put(Rank.FIRST,  rs.getInt("first"));
        map.put(Rank.SECOND, rs.getInt("second"));
        map.put(Rank.THIRD,  rs.getInt("third"));
        map.put(Rank.FOURTH, rs.getInt("fourth"));
        map.put(Rank.FIFTH,  rs.getInt("fifth"));
        map.put(Rank.MISS,   rs.getInt("miss"));

        return map;
    }

    @Override
    public Optional<RoundResult> findRoundResultByRoundId(int roundId) {
        String sql = "SELECT id, first, second, third, fourth, fifth, miss " +
                "FROM round_result WHERE round_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roundId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    int id = rs.getInt("id");

                    // Rank별 결과 Map 생성
                    Map<Rank, Integer> rankResults = buildRankResults(rs);

                    return Optional.of(
                            RoundResult.of(
                                    id,
                                    roundId,
                                    rankResults
                            )
                    );
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("findRoundResultByRoundId 실패", e);
        }
    }

    @Override
    public List<Integer> findAllRoundNumbers() {
        String sql = "SELECT round_number FROM round ORDER BY round_number ASC";
        List<Integer> list = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getInt("round_number"));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("findAllRoundNumbers() 실행 실패", e);
        }
    }

}
