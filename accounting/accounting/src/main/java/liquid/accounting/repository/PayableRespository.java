package liquid.accounting.repository;

import liquid.accounting.domain.Payable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 6/7/15.
 */
public interface PayableRespository extends CrudRepository<Payable, Long> {
    Iterable<Payable> findByChargeId(Long chargeId);

    Iterable<Payable> findByChargeSpId(Long spId);
}
