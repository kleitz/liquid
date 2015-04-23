package liquid.operation.service;

import liquid.operation.domain.ServiceSubtype;
import liquid.core.service.CrudService;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface ServiceSubtypeService extends CrudService<ServiceSubtype> {
    Iterable<ServiceSubtype> findEnabled();

    void add(ServiceSubtype serviceSubtype);
}
