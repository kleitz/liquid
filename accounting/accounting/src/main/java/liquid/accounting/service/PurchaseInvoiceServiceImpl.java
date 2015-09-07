package liquid.accounting.service;

import liquid.accounting.domain.PurchaseInvoice;
import liquid.accounting.repository.PurchaseInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 9/7/15.
 */
@Service
public class PurchaseInvoiceServiceImpl implements InternalPurchaseInvoiceService {

    @Autowired
    private PurchaseInvoiceRepository repository;

    @Override
    public Page<PurchaseInvoice> find(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
