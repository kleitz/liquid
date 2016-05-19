package liquid.accounting.service;

import liquid.accounting.domain.Revenue;
import liquid.accounting.repository.RevenueRepository;
import liquid.core.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 5/19/16.
 */
@Service
public class RevenueServiceImpl extends AbstractService<Revenue, RevenueRepository> implements RevenueService {
    @Override
    public Revenue findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public void doSaveBefore(Revenue entity) {}
}
