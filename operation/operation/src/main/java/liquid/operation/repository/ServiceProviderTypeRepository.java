package liquid.operation.repository;

import liquid.operation.domain.ServiceProviderType;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

/**
 * User: tao
 * Date: 10/25/13
 * Time: 7:33 PM
 */
public interface ServiceProviderTypeRepository extends CrudRepository<ServiceProviderType, Long> {
    /**
     * HACK - Use hibernate query cache that just keeps all ids not entities and get cached entity by id.
     * <a href="http://blog.jhades.org/setup-and-gotchas-of-the-hibernate-second-level-and-query-caches/">
     * Pitfalls of the Hibernate Second-Level/Query Caches
     * </a>
     * <a href="http://www.javalobby.org/java/forums/t48846.html">
     * Truly Understanding the Second-Level and Query Caches
     * </a>
     *
     * @return
     */
    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Iterable<ServiceProviderType> findAll();
}
