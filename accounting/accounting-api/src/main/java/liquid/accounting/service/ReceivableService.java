package liquid.accounting.service;

import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.domain.SalesInvoice;
import liquid.accounting.domain.Receipt;
import liquid.core.model.SearchBarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by mat on 5/19/16.
 */
public interface ReceivableService {
    ReceivableSummary save(ReceivableSummary receivableSummary);

    ReceivableSummary findByCustomerId(Long customerId);

    Page<ReceivableSummary> findAll(Pageable pageable);

    Page<ReceivableSummary> findAll(SearchBarForm searchBarForm, Pageable pageable);

    ReceivableSummary addInvoice(Long customerId, SalesInvoice salesInvoice);

    ReceivableSummary voidInvoice(Long customerId, Long invoiceId);

    ReceivableSummary addReceipt(Long customerId, Receipt receipt);

    ReceivableSummary voidReceipt(Long customerId, Long receiptId);
}
