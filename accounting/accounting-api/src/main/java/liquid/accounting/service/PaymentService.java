package liquid.accounting.service;

import liquid.accounting.domain.Payment;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
public interface PaymentService {
    Payment save(Payment payment);

    List<Payment> findByServiceProviderId(Long serviceProviderId);
}
