package lotto.infrastructure.repository;

import java.sql.*;
import java.util.*;
import lotto.domain.round.entity.Round;
import lotto.domain.round.vo.RoundResult;
import lotto.domain.round.vo.WinningLottoNumbers;
import lotto.domain.round.repository.RoundRepository;
import lotto.domain.lottoTicket.vo.LottoNumber;
import lotto.domain.vo.Rank;
import lotto.infrastructure.db.DBConnectionManager;

public class PostgresRoundRepository implements RoundRepository {

    @Override
    public Optional<Round> findByRoundId(int roundId) {

        Round round = loadBasicRound(roundId);
        if (round == null) return Optional.empty();

        loadWinningNumbersInto(round);
        loadRoundResultInto(round);

        return Optional.of(round);
    }

    @Override
    public Optional<Round> findByRoundNumber(int roundNumber) {

        Integer id = findRoundIdByRoundNumber(roundNumber);
        if (id == null) return Optional.empty();

        return findByRoundId(id); // 애그리거트 완성해서 반환
    }

    @Override
    public Optional<Round> findLatestRound() {

        Integer id = findLatestRoundId();
        if (id == null) return Optional.empty();

        return findByRoundId(id); // 애그리거트 완성해서 반환
    }


    @Override
    public Round saveRound(Round round) {
        String sql = "INSERT INTO round (round_number) VALUES (?) RETURNING id";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, round.getRoundNumber());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id");
                    return new Round(generatedId, round.getRoundNumber());
                }
                throw new RuntimeException("saveRound 실패: id 반환 안됨");
            }

        } catch (SQLException e) {
            throw new RuntimeException("saveRound 실행 실패", e);
        }
    }

    @Override
    public void saveWinningLottoNumbers(WinningLottoNumbers winning) {
        String sql = "INSERT INTO winning_lotto_numbers (round_id, numbers, bonus_number) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, winning.getRoundId());
            pstmt.setString(2, winning.winningNumbersAsCsv());
            pstmt.setInt(3, winning.getBonusNumber().getValue());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("saveWinningLottoNumbers 실패", e);
        }
    }

    @Override
    public void saveRoundResult(RoundResult result) {
        String sql =
                "INSERT INTO round_result (round_id, first, second, third, fourth, fifth, miss) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

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

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("saveRoundResult 실패", e);
        }
    }


    private Round loadBasicRound(int id) {

        String sql = "SELECT id, round_number FROM round WHERE id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) return null;
                return new Round(rs.getInt("id"), rs.getInt("round_number"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("loadBasicRound 실패", e);
        }
    }

    private void loadWinningNumbersInto(Round round) {

        String sql = "SELECT id, numbers, bonus_number FROM winning_lotto_numbers WHERE round_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, round.getId());

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) return;

                int id = rs.getInt("id");
                List<LottoNumber> numbers = parseWinningNumbers(rs.getString("numbers"));
                LottoNumber bonus = LottoNumber.of(rs.getInt("bonus_number"));

                round.registerWinningNumbers(
                        WinningLottoNumbers.of(id, numbers, bonus, round.getId())
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("loadWinningNumbersInto 실패", e);
        }
    }

    private void loadRoundResultInto(Round round) {

        String sql =
                "SELECT id, first, second, third, fourth, fifth, miss " +
                        "FROM round_result WHERE round_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, round.getId());

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) return;

                int id = rs.getInt("id");

                Map<Rank, Integer> map = new EnumMap<>(Rank.class);
                map.put(Rank.FIRST, rs.getInt("first"));
                map.put(Rank.SECOND, rs.getInt("second"));
                map.put(Rank.THIRD, rs.getInt("third"));
                map.put(Rank.FOURTH, rs.getInt("fourth"));
                map.put(Rank.FIFTH, rs.getInt("fifth"));
                map.put(Rank.MISS, rs.getInt("miss"));

                round.registerRoundResult(
                        RoundResult.of(id, round.getId(), map)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("loadRoundResultInto 실패", e);
        }
    }

    private List<LottoNumber> parseWinningNumbers(String csv) {
        return Arrays.stream(csv.split(","))
                .map(s -> LottoNumber.of(Integer.parseInt(s.trim())))
                .toList();
    }

    private Integer findRoundIdByRoundNumber(int roundNumber) {

        String sql = "SELECT id FROM round WHERE round_number = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roundNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("findRoundIdByRoundNumber 실패", e);
        }
    }

    private Integer findLatestRoundId() {

        String sql = "SELECT id FROM round ORDER BY round_number DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt("id");
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("findLatestRoundId 실패", e);
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
            throw new RuntimeException("findAllRoundNumbers 실패", e);
        }
    }
}
