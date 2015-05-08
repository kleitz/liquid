package liquid.accounting.repository;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.core.repository.PageRepository;

/**
 * Created by Tao Ma on 5/6/15.
 */
public interface CashReceiptsJournalRepository extends PageRepository<CashReceiptsJournal> {
    Iterable<CashReceiptsJournal> findByOrderId(Long orderId);
}
