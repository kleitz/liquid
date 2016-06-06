package liquid.accounting.service;

import liquid.accounting.domain.SalesStatement;
import liquid.accounting.repository.SalesStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mat on 6/6/16.
 */
@Service
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    private SalesStatementRepository salesStatementRepository;


    @Override
    public List<SalesStatement> findSalesStatementByCustomerId(Long customerId) {
        return salesStatementRepository.findByCustomerId(customerId);
    }
}
