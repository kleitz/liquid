package liquid.accounting.service;

import liquid.accounting.domain.PurchaseInvoice;
import liquid.accounting.repository.PurchaseInvoiceRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
@Service
public class PurchaseInvoiceServiceImpl extends AbstractService<PurchaseInvoice, PurchaseInvoiceRepository>
        implements PurchaseInvoiceService {

    @Override
    public List<PurchaseInvoice> findByServiceProviderId(Long serviceProviderId) {
        return repository.findByServiceProviderId(serviceProviderId);
    }

    @Override
    public void doSaveBefore(PurchaseInvoice entity) {}
}
