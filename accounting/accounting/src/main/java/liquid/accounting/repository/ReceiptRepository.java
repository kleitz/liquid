package liquid.accounting.repository;

import liquid.accounting.domain.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Tao Ma on 1/8/15.
 */
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    List<Receipt> findByCustomerId(Long customerId);
}
