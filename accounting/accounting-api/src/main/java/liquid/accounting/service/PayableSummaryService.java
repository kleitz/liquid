package liquid.accounting.service;

import liquid.accounting.domain.PayableSummary;
import liquid.accounting.domain.Payment;
import liquid.accounting.domain.PurchaseInvoice;
import liquid.core.model.SearchBarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by mat on 5/23/16.
 */
public interface PayableSummaryService {
    PayableSummary findByServiceProviderId(Long serviceProviderId);

    PayableSummary save(PayableSummary payableSummary);

    Page<PayableSummary> findAll(Pageable pageable);

    Page<PayableSummary> findAll(SearchBarForm searchBarForm, Pageable pageable);

    PayableSummary addInvoice(Long serviceProviderId, PurchaseInvoice purchaseInvoice);

    PayableSummary voidInvoice(Long serviceProviderId, Long invoiceId);

    PayableSummary addPayment(Long serviceProviderId, Payment payment);

    PayableSummary voidPayment(Long serviceProviderId, Long paymentId);
}
