package liquid.accounting.repository;

import liquid.accounting.domain.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 *  
 * User: tao
 * Date: 11/16/13
 * Time: 1:44 PM
 */
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    ExchangeRate findByMonth(Date month);
}
