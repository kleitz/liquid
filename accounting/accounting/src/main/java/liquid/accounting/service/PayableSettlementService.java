package liquid.accounting.service;

import liquid.accounting.domain.PayableSettlementEntity;
import liquid.accounting.repository.PayableSettlementRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 11/15/14.
 */
@Service
public class PayableSettlementService extends AbstractService<PayableSettlementEntity, PayableSettlementRepository> {
    @Override
    public void doSaveBefore(PayableSettlementEntity entity) { }

    public Iterable<PayableSettlementEntity> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
