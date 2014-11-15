package liquid.facade;

import liquid.domain.ServiceProvider;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceProviderTypeEnity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ServiceProviderService;
import liquid.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by redbrick9 on 6/9/14.
 */
@Service
public class ServiceProviderFacade {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public ServiceProviderEntity save(ServiceProvider serviceProvider) {
        ServiceProviderEntity entity = convert(serviceProvider);
        return serviceProviderService.save(entity);
    }

    public ServiceProvider find(long id) {
        ServiceProviderEntity entity = serviceProviderService.find(id);
        ServiceProvider serviceProvider = convert(entity);
        return serviceProvider;
    }

    public List<ServiceProvider> findBySubtypeId(Long subtypeId) {
        ServiceSubtypeEntity subtypeEntity = serviceSubtypeService.find(subtypeId);
        List<ServiceProviderEntity> providerEntities = subtypeEntity.getServiceProviders();
        List<ServiceProvider> serviceProviders = new ArrayList<>();
        for (ServiceProviderEntity providerEntity : providerEntities) {
            serviceProviders.add(convert(providerEntity));
        }
        return serviceProviders;
    }

    @Transactional("transactionManager")
    public List<ServiceProvider> findByQueryNameLike(String name) {
        List<ServiceProvider> list = new ArrayList<>();
        Iterable<ServiceProviderEntity> entities = serviceProviderService.findByQueryNameLike(name);
        for (ServiceProviderEntity entity : entities) {
            list.add(convert(entity));
        }
        return list;
    }

    public void changeStatus(Long id, Integer status) {
        ServiceProviderEntity entity = serviceProviderService.find(id);
        entity.setStatus(status);
        serviceProviderService.save(entity);
    }

    private ServiceProviderEntity convert(ServiceProvider serviceProvider) {
        ServiceProviderEntity entity = new ServiceProviderEntity();
        entity.setId(serviceProvider.getId());
        entity.setCode(serviceProvider.getCode());
        entity.setName(serviceProvider.getName());
        entity.setType(ServiceProviderTypeEnity.newInstance(ServiceProviderTypeEnity.class, serviceProvider.getTypeId()));
        entity.setAddress(serviceProvider.getAddress());
        entity.setPostcode(serviceProvider.getPostcode());
        entity.setContact(serviceProvider.getContact());
        entity.setPhone(serviceProvider.getPhone());
        entity.setCell(serviceProvider.getCell());

        for (Long subtypeId : serviceProvider.getSubtypeIds()) {
            ServiceSubtypeEntity serviceSubtypeEntity = serviceSubtypeService.find(subtypeId);
            serviceSubtypeEntity.getServiceProviders().add(entity);
            entity.getServiceSubtypes().add(serviceSubtypeEntity);
        }
        return entity;
    }

    private ServiceProvider convert(ServiceProviderEntity entity) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(entity.getId());
        serviceProvider.setCode(entity.getCode());
        serviceProvider.setName(entity.getName());
        serviceProvider.setTypeId(entity.getType().getId());
        serviceProvider.setAddress(entity.getAddress());
        serviceProvider.setPostcode(entity.getPostcode());
        serviceProvider.setContact(entity.getContact());
        serviceProvider.setPhone(entity.getPhone());
        serviceProvider.setCell(entity.getCell());

        Set<ServiceSubtypeEntity> serviceSubtypeEntities = entity.getServiceSubtypes();
        Long[] subtypeIds = new Long[serviceSubtypeEntities.size()];
        int i = 0;
        for (ServiceSubtypeEntity serviceSubtypeEntity : serviceSubtypeEntities) {
            subtypeIds[i] = serviceSubtypeEntity.getId();
            i++;
        }
        serviceProvider.setSubtypeIds(subtypeIds);
        serviceProvider.setStatus(entity.getStatus());
        return serviceProvider;
    }
}
