package liquid.accounting.service;

import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.model.Earning;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceivableSummaryService {
    ReceivableSummary findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);

    ReceivableSummary save(ReceivableSummary receivableSummary);

    Earning calculateEarning(Long orderId);
}
