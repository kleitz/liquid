package liquid.operation.service;

import liquid.core.service.AbstractService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceProviderType;
import liquid.operation.domain.ServiceProvider_;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.repository.ServiceProviderRepository;
import liquid.operation.repository.ServiceProviderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * User: tao
 * Date: 10/13/13
 * Time: 12:56 AM
 */
@Service
public class ServiceProviderServiceImpl extends AbstractService<ServiceProvider, ServiceProviderRepository>
        implements InternalServiceProviderService {

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderTypeRepository serviceProviderTypeRepository;

    @Override
    public void doSaveBefore(ServiceProvider serviceProvider) {
    }

    @Override
    public Page<ServiceProvider> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional("transactionManager")
    public Iterable<ServiceProvider> findAll() {
        Iterable<ServiceProvider> serviceProviders = repository.findOrderByName();
        for (ServiceProvider serviceProvider : serviceProviders) {
            serviceProvider.getSubtypes().size();
        }
        return serviceProviders;
    }

    @Override
    @Transactional("transactionManager")
    public ServiceProvider find(Long id) {
        ServiceProvider entity = repository.findOne(id);
        Set<ServiceSubtype> serviceSubtypeEntitySet = entity.getSubtypes();
        serviceSubtypeEntitySet.size();
        return entity;
    }

    @Override
    public Iterable<ServiceProvider> findByType(Long typeId) {
        ServiceProviderType type = serviceProviderTypeRepository.findOne(typeId);
        return repository.findByType(type);
    }

    public Iterable<ServiceProvider> findByType(ServiceProviderType type) {
        return repository.findByType(type);
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<ServiceProviderType> iterable = serviceProviderTypeRepository.findAll();
        for (ServiceProviderType spType : iterable) {
            spTypes.put(spType.getId(), spType.getName());
        }
        return spTypes;
    }

    @Override
    public Iterable<ServiceProvider> findByQueryNameLike(String name) {
        return repository.findByQueryNameLike("%" + name + "%");
    }

    @Override
    public List<ServiceProvider> findByServiceSubtypeId(Long serviceSubtypeId) {
        HashSet<ServiceSubtype> serviceSubtypes = new HashSet<>();
        serviceSubtypes.add(ServiceSubtype.newInstance(ServiceSubtype.class, serviceSubtypeId));
        return repository.findBySubtypes(serviceSubtypes);
    }

    @Override
    public Page<ServiceProvider> findAll(String name, Pageable pageable) {
        List<Specification<ServiceProvider>> specList = new ArrayList<>();

        if (null != name) {
            Specification<ServiceProvider> orderNoSpec = new Specification<ServiceProvider>() {
                @Override
                public Predicate toPredicate(Root<ServiceProvider> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(ServiceProvider_.name), "%" + name + "%");
                }
            };
            specList.add(orderNoSpec);
        }

        if (specList.size() == 0) {
            return repository.findAll(pageable);
        } else {
            Specifications<ServiceProvider> specs = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specs.and(specList.get(i));
            }
            return repository.findAll(specs, pageable);
        }
    }
}
