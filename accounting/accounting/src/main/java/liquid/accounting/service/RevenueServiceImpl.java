package liquid.accounting.service;

import liquid.accounting.domain.Revenue;
import liquid.accounting.repository.RevenueRepository;
import liquid.core.model.SearchBarForm;
import liquid.core.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Revenue> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Revenue> findAll(SearchBarForm searchBarForm, Pageable pageable) {
        return null;
    }

    @Override
    public void doSaveBefore(Revenue entity) {}
}
