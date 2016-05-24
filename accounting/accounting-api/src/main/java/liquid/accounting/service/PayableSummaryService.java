package liquid.accounting.service;

import liquid.accounting.domain.PayableSummary;

/**
 * Created by mat on 5/23/16.
 */
public interface PayableSummaryService {
    PayableSummary findByServiceProviderId(Long serviceProviderId);

    PayableSummary save(PayableSummary payableSummary);
}
