package liquid.operation.repository;

import liquid.operation.domain.ServiceSubtype;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

/**
 * Created by redbrick9 on 5/9/14.
 */
public interface ServiceSubtypeRepository extends CrudRepository<ServiceSubtype, Long> {
    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Iterable<ServiceSubtype> findAll();
}
