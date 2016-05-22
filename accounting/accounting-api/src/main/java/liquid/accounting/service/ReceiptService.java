package liquid.accounting.service;

import liquid.accounting.domain.Receipt;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ReceiptService {
    Receipt update(Receipt receipt);

    Receipt save(Receipt receipt);

    List<Receipt> findByCustomerId(Long customerId);
}
