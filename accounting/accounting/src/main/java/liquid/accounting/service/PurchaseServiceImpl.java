package liquid.accounting.service;

import liquid.accounting.domain.Purchase;
import liquid.accounting.domain.PurchaseStatus;
import liquid.accounting.repository.ChargeRepository;
import liquid.accounting.repository.PurchaseRepository;
import liquid.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 9/27/14.
 */
@Service
public class PurchaseServiceImpl extends AbstractService<Purchase, PurchaseRepository> implements PurchaseService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public void doSaveBefore(Purchase entity) {
    }

    @Deprecated
    @Override
    public void deleteByLegId(Long legId) {
        chargeRepository.deleteByLegId(legId);
    }

    @Override
    public List<Purchase> findByOrderId(Long orderId) {
        return purchaseRepository.findByOrderId(orderId);
    }

    @Override
    public Purchase addOne(Purchase purchase) {
        purchase.setStatus(PurchaseStatus.VALID.ordinal());
        return save(purchase);
    }

    @Override
    public Purchase voidOne(Purchase purchase) {
        Purchase originalOne = purchaseRepository.findOne(purchase.getId());
        originalOne.setStatus(PurchaseStatus.INVALID.ordinal());
        originalOne.setComment(purchase.getComment());
        return purchaseRepository.save(originalOne);
    }
}
