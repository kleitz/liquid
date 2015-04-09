package liquid.service;

import liquid.core.repository.PageRepository;
import liquid.core.domain.BaseUpdateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 11/26/14.
 */
public abstract class StandardCrudService<E extends BaseUpdateEntity> extends AbstractService<E, PageRepository<E>> {
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
