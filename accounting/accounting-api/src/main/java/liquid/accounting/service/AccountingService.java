package liquid.accounting.service;

import liquid.accounting.domain.SalesStatement;

import java.util.List;

/**
 * Created by mat on 6/6/16.
 */
public interface AccountingService {

    List<SalesStatement> findSalesStatementByCustomerId(Long customerId);

    SalesStatement save(Long customerId, Long[] orderIds);
}
