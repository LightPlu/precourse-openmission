package lotto.domain.lottoTicket.repository;

import java.util.List;
import lotto.domain.lottoTicket.entity.LottoTicket;

public interface LottoTicketRepository {

    void saveTickets(List<LottoTicket> ticket);

    List<LottoTicket> findTicketsByRoundId(int roundId);

}
