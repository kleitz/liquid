package liquid.accounting.service;

import liquid.accounting.domain.Revenue;

/**
 * Created by mat on 5/19/16.
 */
public interface RevenueService {
    Revenue save(Revenue revenue);

    Revenue findByCustomerId(Long customerId);
}
