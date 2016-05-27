package liquid.accounting.service;

import liquid.accounting.domain.ReceivableSummaryObsolete;
import liquid.accounting.model.Earning;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceivableSummaryObsoloteService {
    ReceivableSummaryObsolete findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);

    ReceivableSummaryObsolete save(ReceivableSummaryObsolete receivableSummary);

    Earning calculateEarning(Long orderId);
}
