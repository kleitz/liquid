package liquid.accounting.service;

import liquid.accounting.domain.SalesInvoice;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface SalesInvoiceService {
    SalesInvoice update(SalesInvoice salesInvoice);

    SalesInvoice save(SalesInvoice salesInvoice);

    List<SalesInvoice> findByCustomerId(Long customerId);
}
