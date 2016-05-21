package liquid.accounting.service;

import liquid.accounting.domain.Receipt;
import liquid.accounting.repository.ReceiptRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptServiceImpl extends AbstractService<Receipt, ReceiptRepository> {

    @Override
    public void doSaveBefore(Receipt entity) {}

    public Iterable<Receipt> findByCustomerId(Long payerId) {
        return repository.findByCustomerId(payerId);
    }
}
