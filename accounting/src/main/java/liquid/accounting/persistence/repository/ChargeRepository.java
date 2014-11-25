package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.ChargeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 8:37 PM
 */
public interface ChargeRepository extends CrudRepository<ChargeEntity, Long>  {
    List<ChargeEntity> findByTaskId(String taskId);

    Iterable<ChargeEntity> findByOrderId(long orderId);

    Page<ChargeEntity> findByOrderId(long orderId, Pageable pageable);

    Iterable<ChargeEntity> findByOrderOrderNoLike(String orderNo);

    Iterable<ChargeEntity> findBySpNameLike(String cumtomerName);

    Page<ChargeEntity> findBySpNameLike(String cumtomerName, Pageable pageable);

    Iterable<ChargeEntity> findByLegId(long legId);

    Iterable<ChargeEntity> findByRouteId(long routeId);

    Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole);

    Page<ChargeEntity> findAll(Pageable pageable);

    Page<ChargeEntity> findAll(Specification<ChargeEntity> specification, Pageable pageable);

    void deleteByLegId(Long legId);
}