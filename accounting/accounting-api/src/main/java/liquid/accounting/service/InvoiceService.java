package liquid.accounting.service;

import liquid.accounting.domain.Invoice;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InvoiceService {
    Invoice update(Invoice invoice);

    Invoice save(Invoice invoice);
}
