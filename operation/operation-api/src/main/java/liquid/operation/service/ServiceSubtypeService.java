package liquid.operation.service;

import liquid.operation.domain.ServiceSubtype;
import liquid.core.service.CrudService;

import java.util.List;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface ServiceSubtypeService extends CrudService<ServiceSubtype> {
    List<ServiceSubtype> findEnabled();

    void add(ServiceSubtype serviceSubtype);
}
