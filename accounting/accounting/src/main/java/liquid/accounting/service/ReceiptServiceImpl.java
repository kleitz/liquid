package liquid.accounting.service;

import liquid.accounting.domain.ReceiptEntity;
import liquid.accounting.repository.ReceiptRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptServiceImpl extends AbstractService<ReceiptEntity, ReceiptRepository> {

    @Override
    public void doSaveBefore(ReceiptEntity entity) {}

    public Iterable<ReceiptEntity> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Iterable<ReceiptEntity> findByPayerId(Long payerId) {
        return repository.findByPayerId(payerId);
    }
}
