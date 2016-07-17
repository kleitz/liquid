package liquid.accounting.service;

import liquid.accounting.domain.PurchaseStatement;

import java.util.List;

/**
 * Created by mat on 7/17/16.
 */
public interface PurchaseStatementService {
    List<PurchaseStatement> findSalesStatementByServiceProviderId(Long serviceProviderId);

    PurchaseStatement save(Long serviceProviderId, Long[] orderIds);

    PurchaseStatement find(Long id);
}
