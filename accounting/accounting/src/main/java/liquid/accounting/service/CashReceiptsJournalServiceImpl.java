package liquid.accounting.service;

import liquid.accounting.domain.CashReceiptsJournal;
import liquid.accounting.domain.ReceivableSummaryObsolete;
import liquid.accounting.repository.CashReceiptsJournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ReceivableSummaryObsoloteService receivableSummaryService;

    @Override
    public Iterable<CashReceiptsJournal> findByOrderId(Long orderId) {
        return cashReceiptsJournalRepository.findByOrderId(orderId);
    }

    @Override
    public Iterable<CashReceiptsJournal> findByCustomerId(Long customerId) {
        return cashReceiptsJournalRepository.findByOrderCustomerId(customerId);
    }

    @Transactional("transactionManager")
    @Override
    public CashReceiptsJournal save(CashReceiptsJournal cashReceiptsJournal) {
        ReceivableSummaryObsolete receivableSummary = receivableSummaryService.findByOrderId(cashReceiptsJournal.getOrder().getId());
        receivableSummary.setCny(receivableSummary.getCny().add(cashReceiptsJournal.getInvoicedAmt() == null ? BigDecimal.ZERO : cashReceiptsJournal.getInvoicedAmt()));
        receivableSummary.setPaidCny(receivableSummary.getPaidCny() + (cashReceiptsJournal.getReceivedAmt() == null ? 0L : cashReceiptsJournal.getReceivedAmt().longValue()));
        receivableSummary.setInvoicedCny(receivableSummary.getInvoicedCny() + (cashReceiptsJournal.getInvoicedAmt() == null ? 0L : cashReceiptsJournal.getInvoicedAmt().longValue()));
        receivableSummaryService.save(receivableSummary);
        return cashReceiptsJournalRepository.save(cashReceiptsJournal);
    }
}
