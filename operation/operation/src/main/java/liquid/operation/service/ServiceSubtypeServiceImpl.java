package liquid.operation.service;

import liquid.operation.domain.ServiceSubtype;
import liquid.operation.repository.ServiceSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Service
public class ServiceSubtypeServiceImpl implements ServiceSubtypeService {

    @Autowired
    private ServiceSubtypeRepository serviceSubtypeRepository;

    @Override
    public List<ServiceSubtype> findEnabled() {
        return serviceSubtypeRepository.findAll();
    }

    @Override
    public void add(ServiceSubtype serviceSubtype) {
        serviceSubtypeRepository.save(serviceSubtype);
    }

    @Override
    public ServiceSubtype find(Long id) {
        return serviceSubtypeRepository.findOne(id);
    }

    @Override
    public ServiceSubtype disable(Long id) {
        ServiceSubtype serviceSubtype = find(id);
        serviceSubtype.setStatus(1);
        return serviceSubtypeRepository.save(serviceSubtype);
    }

    @Override
    public ServiceSubtype enable(Long id) {
        ServiceSubtype serviceSubtype = find(id);
        serviceSubtype.setStatus(0);
        return serviceSubtypeRepository.save(serviceSubtype);
    }
}
