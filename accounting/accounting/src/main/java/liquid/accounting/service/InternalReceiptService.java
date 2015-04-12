package liquid.accounting.service;

import liquid.accounting.model.Receipt;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InternalReceiptService extends ReceiptService {
    Receipt save(Receipt receipt);
}
