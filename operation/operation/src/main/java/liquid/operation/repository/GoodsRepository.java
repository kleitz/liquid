package liquid.operation.repository;

import liquid.core.repository.PageRepository;
import liquid.operation.domain.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *  
 * User: tao
 * Date: 9/28/13
 * Time: 3:31 PM
 */

public interface GoodsRepository extends PageRepository<Goods> {
    Page<Goods> findAll(Pageable pageable);
}
