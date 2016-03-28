package liquid.accounting.service;

import liquid.accounting.domain.Purchase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 9/27/14.
 */
@Service
public interface PurchaseService {
    @Deprecated
    void deleteByLegId(Long legId);

    List<Purchase> findByOrderId(Long orderId);

    Purchase save(Purchase purchase);
}
