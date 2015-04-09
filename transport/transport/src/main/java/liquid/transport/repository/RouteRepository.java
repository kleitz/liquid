package liquid.transport.repository;

import liquid.core.PageRepository;
import liquid.transport.domain.RouteEntity;

import java.util.List;

/**
 * Created by mat on 11/26/14.
 */
public interface RouteRepository extends PageRepository<RouteEntity> {
    List<RouteEntity> findByFromIdAndToId(Long fromId, Long toId);
}
