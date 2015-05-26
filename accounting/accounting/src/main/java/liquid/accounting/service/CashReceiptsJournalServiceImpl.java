package liquid.accounting.service;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.repository.CashReceiptsJournalRepository;
import liquid.core.model.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by Tao Ma on 5/8/15.
 */
@Service
public class CashReceiptsJournalServiceImpl implements InternalCashReceiptsJournalService {

    @Autowired
    private CashReceiptsJournalRepository cashReceiptsJournalRepository;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Override
    public Iterable<CashReceiptsJournal> findByOrderId(Long orderId) {
        return cashReceiptsJournalRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    @Override
    public CashReceiptsJournal save(CashReceiptsJournal cashReceiptsJournal) {
        ReceivableSummary receivableSummary = receivableSummaryService.findByOrderId(cashReceiptsJournal.getOrder().getId());
        receivableSummary.setCny(receivableSummary.getCny().add(cashReceiptsJournal.getInvoicedAmt() == null ? BigDecimal.ZERO : cashReceiptsJournal.getInvoicedAmt()));
        receivableSummary.setPaidCny(receivableSummary.getPaidCny() + (cashReceiptsJournal.getReceivedAmt() == null ? 0L : cashReceiptsJournal.getReceivedAmt().longValue()));
        receivableSummary.setInvoicedCny(receivableSummary.getInvoicedCny() + (cashReceiptsJournal.getInvoicedAmt() == null ? 0L : cashReceiptsJournal.getInvoicedAmt().longValue()));
        receivableSummaryService.save(receivableSummary);
        return cashReceiptsJournalRepository.save(cashReceiptsJournal);
    }

    @Override
    public Iterable<CashReceiptsJournal> findAll(SearchBarForm searchBarForm) {
        return null;
    }

    @Override
    public Page<CashReceiptsJournal> findAll(SearchBarForm searchBar, Pageable pageable) {
        return null;
    }
}
