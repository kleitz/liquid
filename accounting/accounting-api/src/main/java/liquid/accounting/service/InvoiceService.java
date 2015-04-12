package liquid.accounting.service;

import liquid.accounting.model.Invoice;
import liquid.accounting.model.Statement;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InvoiceService {
    Statement<Invoice> findByOrderId(Long orderId);

    Invoice update(Invoice invoice);
}
