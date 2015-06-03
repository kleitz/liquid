package liquid.accounting.service;

import liquid.accounting.domain.CashReceiptsJournal;

/**
 * Created by Tao Ma on 5/6/15.
 */
public interface InternalCashReceiptsJournalService {
    Iterable<CashReceiptsJournal> findByOrderId(Long orderId);

    Iterable<CashReceiptsJournal> findByCustomerId(Long customerId);

    CashReceiptsJournal save(CashReceiptsJournal cashReceiptsJournal);
}
