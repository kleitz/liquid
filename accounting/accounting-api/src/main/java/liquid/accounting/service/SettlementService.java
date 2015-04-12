package liquid.accounting.service;

import liquid.accounting.model.Settlement;
import liquid.accounting.model.Statement;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface SettlementService {
    Statement<Settlement> findByOrderId(Long orderId);

    Settlement save(Settlement settlement);
}
