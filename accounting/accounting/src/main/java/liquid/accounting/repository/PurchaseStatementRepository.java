package liquid.accounting.repository;

import liquid.accounting.domain.PurchaseStatement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 7/17/16.
 */
public interface PurchaseStatementRepository  extends CrudRepository<PurchaseStatement, Long> {
    List<PurchaseStatement> findByServiceProviderId(Long serviceProviderId);
}
