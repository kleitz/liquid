package liquid.operation.service;

import liquid.core.service.AbstractCachedService;
import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.domain.LocationType_;
import liquid.operation.domain.Location_;
import liquid.operation.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 *  
 * User: tao
 * Date: 10/13/13
 * Time: 4:59 PM
 */
@Service
public class LocationServiceImpl extends AbstractCachedService<Location, LocationRepository>
        implements InternalLocationService {
    @Override
    public void doSaveBefore(Location entity) { }

    @Override
    public Page<Location> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Location> findAll(final Byte typeId, Pageable pageable) {
        return repository.findByTypeId(typeId, pageable);
    }


    @Override
    public Location save(Location location) {
        return super.save(location);
    }

    @Override
    public Iterable<Location> findAll() {
        return super.findAll();
    }

    @Override
    public Page<Location> findAll(final String name, final Byte typeId, Pageable pageable) {
        List<Specification<Location>> specList = new ArrayList<>();

        if (null != name) {
            Specification<Location> orderNoSpec = new Specification<Location>() {
                @Override
                public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(Location_.name), "%" + name + "%");
                }
            };
            specList.add(orderNoSpec);
        }

        if (null != typeId) {
            Specification<Location> orderNoSpec = new Specification<Location>() {
                @Override
                public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(Location_.type).get(LocationType_.id), typeId);
                }
            };
            specList.add(orderNoSpec);
        }

        if(specList.size() == 0) {
            return repository.findAll(pageable);
        } else {
            Specifications<Location> specs = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specs.and(specList.get(i));
            }
            return repository.findAll(specs, pageable);
        }
    }

    @Override
    public List<Location> findYards() {
        return findByTypeId(LocationType.YARD);
    }

    @Override
    public List<Location> findByTypeId(Byte typeId) {
        return repository.findByTypeId(typeId);
    }

    @Override
    public Iterable<Location> findByNameLike(String name) {
        Iterable<Location> locations = repository.findByQueryNameLike("%" + name + "%");
        return locations;
    }

    @Override
    public Iterable<Location> findByTypeAndNameLike(Byte typeId, String name) {
        Iterable<Location> locations = repository.findByTypeIdAndQueryNameLike(typeId, "%" + name + "%");
        return locations;
    }

    @Override
    public Location findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Location findByTypeAndName(Byte typeId, String name) {
        return repository.findByTypeIdAndName(typeId, name);
    }
}
