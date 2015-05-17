package liquid.accounting.service;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.core.model.SearchBarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 5/6/15.
 */
public interface InternalCashReceiptsJournalService {
    Iterable<CashReceiptsJournal> findByOrderId(Long orderId);

    CashReceiptsJournal save(CashReceiptsJournal cashReceiptsJournal);

    Iterable<CashReceiptsJournal> findAll(SearchBarForm searchBarForm);

    Page<CashReceiptsJournal> findAll(SearchBarForm searchBar, Pageable pageable);
}
