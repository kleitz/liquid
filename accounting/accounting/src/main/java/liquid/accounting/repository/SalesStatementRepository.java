package liquid.accounting.repository;

import liquid.accounting.domain.SalesStatement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 6/6/16.
 */
public interface SalesStatementRepository extends CrudRepository<SalesStatement, Long> {
    List<SalesStatement> findByCustomerId(Long customerId);
}
