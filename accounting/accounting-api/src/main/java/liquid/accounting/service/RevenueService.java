package liquid.accounting.service;

import liquid.accounting.domain.Revenue;
import liquid.core.model.SearchBarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by mat on 5/19/16.
 */
public interface RevenueService {
    Revenue save(Revenue revenue);

    Revenue findByCustomerId(Long customerId);

    Page<Revenue> findAll(Pageable pageable);

    Page<Revenue> findAll(SearchBarForm searchBarForm, Pageable pageable);
}
