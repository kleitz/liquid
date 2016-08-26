package liquid.accounting.service;

import liquid.accounting.domain.PurchaseStatement;

import java.util.List;

/**
 * Created by mat on 7/17/16.
 */
public interface PurchaseStatementService {
    List<PurchaseStatement> findStatementByServiceProviderId(Long serviceProviderId);

    PurchaseStatement save(Long serviceProviderId, Long statementId, Long[] orderIds);

    PurchaseStatement find(Long id);
}
