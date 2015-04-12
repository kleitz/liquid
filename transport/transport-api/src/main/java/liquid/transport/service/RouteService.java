package liquid.transport.service;

import liquid.transport.domain.RouteEntity;

import java.util.List;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface RouteService {
    List<RouteEntity> find(Long fromId, Long toId);

    RouteEntity find(Long id);

    RouteEntity save(RouteEntity routeEntity);

    void delete(Long id);
}
