package liquid.accounting.service;

import liquid.accounting.model.Receipt;
import liquid.accounting.model.Statement;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceiptService {
    Statement<Receipt> findByOrderId(Long orderId);

    Receipt update(Receipt receipt);
}
