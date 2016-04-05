package liquid.operation.service;

import liquid.core.service.AbstractCachedService;
import liquid.operation.domain.Customer;
import liquid.operation.domain.Customer_;
import liquid.operation.repository.CustomerRepository;
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
 * Time: 6:28 PM
 */
@Service
public class CustomerServiceImpl extends AbstractCachedService<Customer, CustomerRepository>
        implements InternalCustomerService {
    @Override
    public void doSaveBefore(Customer entity) { }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Customer findByName(String name) {
        return repository.findByName(name);
    }

    public Iterable<Customer> findByNameLike(String name) {
        Iterable<Customer> customers = repository.findByQueryNameLike("%" + name + "%");
        return customers;
    }

    @Override
    public Page<Customer> findByQueryNameLike(String name, Pageable pageable) {
        return repository.findByQueryNameLike("%" + name + "%", pageable);
    }

    @Override
    public Page<Customer> findAll(String name, Pageable pageable) {
        List<Specification<Customer>> specList = new ArrayList<>();

        if (null != name) {
            Specification<Customer> orderNoSpec = new Specification<Customer>() {
                @Override
                public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(Customer_.name), "%" + name + "%");
                }
            };
            specList.add(orderNoSpec);
        }

        if (specList.size() == 0) {
            return repository.findAll(pageable);
        } else {
            Specifications<Customer> specs = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specs.and(specList.get(i));
            }
            return repository.findAll(specs, pageable);
        }
    }

    @Override
    public Iterable<Customer> findByQueryNameLike(String name) {
        List<Customer> result = new ArrayList<>();

        Iterable<Customer> entities = super.findAll();
        for (Customer entity : entities) {
            int index = entity.getQueryName().indexOf(name);
            if (index > -1) result.add(entity);
        }
        return result;
    }
}
