package liquid.order.service;

import liquid.operation.domain.Customer_;
import liquid.operation.domain.Location;
import liquid.order.domain.ReceivingOrder;
import liquid.order.domain.ReceivingOrder_;
import liquid.order.repository.ReceivingContainerRepository;
import liquid.order.repository.ReceivingOrderRepository;
import liquid.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * User: tao
 * Date: 10/13/13
 * Time: 4:57 PM
 */
@Service
public class ReceivingOrderServiceImpl extends AbstractBaseOrderService<ReceivingOrder, ReceivingOrderRepository> {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderServiceImpl.class);

    // Repositories
    @Autowired
    private ReceivingOrderRepository recvOrderRepository;

    @Autowired
    private ReceivingContainerRepository recvContainerRepository;

    public ReceivingOrder newOrder(List<Location> locationEntities) {
        ReceivingOrder order = new ReceivingOrder();
        Location second = CollectionUtil.tryToGet2ndElement(locationEntities);
        order.setSource(second);
        return order;
    }

    @Override
    public void doSaveBefore(ReceivingOrder entity) {}

    public List<ReceivingOrder> findAllOrderByDesc() {
        return recvOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public Iterable<ReceivingOrder> findByOrderNo(String orderNo) {
        return recvOrderRepository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Page<ReceivingOrder> findByOrderNoLike(String orderNo, Pageable pageable) {
        return recvOrderRepository.findByOrderNoLike("%" + orderNo + "%", pageable);
    }

    public Page<ReceivingOrder> findAll(Pageable pageable) {
        return recvOrderRepository.findAll(pageable);
    }

    public Page<ReceivingOrder> findByCustomerId(Long customerId, String createdBy, Pageable pageable) {
        return recvOrderRepository.findByCustomerIdAndCreatedBy(customerId, createdBy, pageable);
    }

    /**
     * Criteria Query for order.
     *
     * @param id
     * @param customerId
     * @param username
     * @param pageable
     * @return
     */
    public Page<ReceivingOrder> findAll(final Long id, final Long customerId, final String username, final Pageable pageable) {
        List<Specification<ReceivingOrder>> specList = new ArrayList<>();

        if (null != id) {
            Specification<ReceivingOrder> orderNoSpec = new Specification<ReceivingOrder>() {
                @Override
                public Predicate toPredicate(Root<ReceivingOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(ReceivingOrder_.id), id);
                }
            };
            specList.add(orderNoSpec);
        }

        if (null != customerId) {
            Specification<ReceivingOrder> customerNameSpec = new Specification<ReceivingOrder>() {
                @Override
                public Predicate toPredicate(Root<ReceivingOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(ReceivingOrder_.customer).get(Customer_.id), customerId);
                }
            };
            specList.add(customerNameSpec);
        }

        if (null != username) {
            Specification<ReceivingOrder> usernameSpec = new Specification<ReceivingOrder>() {
                @Override
                public Predicate toPredicate(Root<ReceivingOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(ReceivingOrder_.createdBy), username);
                }
            };
        }

        if (specList.size() > 0) {
            Specifications<ReceivingOrder> specifications = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specifications.and(specList.get(i));
            }
            return repository.findAll(specifications, pageable);
        }

        return repository.findAll(pageable);
    }
}
