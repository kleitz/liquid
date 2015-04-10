package liquid.accounting.facade;

import liquid.accounting.model.Earning;
import liquid.accounting.model.ReceivableSummary;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public interface ReceivableFacade {
    ReceivableSummary save(ReceivableSummary receivableSummary);

    Earning calculateEarning(Long orderId);
}
