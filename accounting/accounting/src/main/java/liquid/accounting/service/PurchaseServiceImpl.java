package liquid.accounting.service;

import liquid.accounting.domain.Purchase;
import liquid.accounting.repository.ChargeRepository;
import liquid.accounting.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 9/27/14.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

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
    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
