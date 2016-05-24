package liquid.accounting.service;

import liquid.accounting.domain.Payment;
import liquid.accounting.repository.PaymentRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
@Service
public class PaymentServiceImpl extends AbstractService<Payment, PaymentRepository> implements PaymentService{
    @Override
    public List<Payment> findByServiceProviderId(Long serviceProviderId) {
        return repository.findByServiceProviderId(serviceProviderId);
    }

    @Override
    public void doSaveBefore(Payment entity) {

    }
}
