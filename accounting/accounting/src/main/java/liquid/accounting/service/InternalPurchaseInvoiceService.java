package liquid.accounting.service;

import liquid.accounting.domain.PurchaseInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 9/7/15.
 */
public interface InternalPurchaseInvoiceService extends PurchaseInvoiceService {
    Page<PurchaseInvoice> find(Pageable pageable);

    PurchaseInvoice save(PurchaseInvoice purchaseInvoice);
}
