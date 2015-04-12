package liquid.accounting.service;

import liquid.accounting.domain.ReceivableSummaryEntity;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceivableSummaryService {
    ReceivableSummaryEntity findByOrderId(Long orderId);

    ReceivableSummaryEntity save(ReceivableSummaryEntity receivableSummaryEntity);
}
