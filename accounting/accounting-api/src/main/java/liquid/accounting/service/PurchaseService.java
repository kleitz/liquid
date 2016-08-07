package liquid.accounting.service;

import liquid.accounting.domain.Purchase;
import liquid.core.model.SearchBarForm;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 9/27/14.
 */
@Service
public interface PurchaseService {
    @Deprecated
    void deleteByLegId(Long legId);

    Purchase find(Long id);

    List<Purchase> findByOrderId(Long orderId);

    List<Purchase> findByServiceProviderId(Long serviceProviderId, SearchBarForm searchBarForm);

    Purchase save(Purchase purchase);

    Purchase addOne(Purchase purchase);

    Purchase voidOne(Purchase purchase);
}
