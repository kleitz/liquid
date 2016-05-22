package liquid.accounting.service;

import liquid.accounting.domain.Invoice;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InvoiceService {
    Invoice update(Invoice invoice);

    Invoice save(Invoice invoice);

    List<Invoice> findByCustomerId(Long customerId);
}
