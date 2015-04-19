package liquid.accounting.repository;

import liquid.accounting.domain.ExchangeRateEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *  
 * User: tao
 * Date: 11/16/13
 * Time: 1:44 PM
 */
public interface ExchangeRateRepository extends CrudRepository<ExchangeRateEntity, Long> {}
