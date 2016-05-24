package liquid.accounting.repository;

import liquid.accounting.domain.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    List<Payment> findByServiceProviderId(Long serviceProviderId);
}
