package liquid.accounting.service;

import liquid.accounting.domain.Receipt;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceiptService {
    Receipt update(Receipt receipt);

    Receipt save(Receipt receipt);
}
