package liquid.transport.service;

import liquid.transport.domain.PathEntity;
import liquid.transport.domain.RouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InternalRouteService extends RouteService {
    Page<RouteEntity> findAll(Pageable pageable);

    RouteEntity addPath(Long routeId, PathEntity newPath);
}
