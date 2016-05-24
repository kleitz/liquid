package liquid.accounting.service;

import liquid.accounting.domain.PurchaseInvoice;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
public interface PurchaseInvoiceService {
    PurchaseInvoice save(PurchaseInvoice salesInvoice);

    List<PurchaseInvoice> findByServiceProviderId(Long serviceProviderId);
}
