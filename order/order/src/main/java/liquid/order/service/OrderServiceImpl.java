package liquid.order.service;

import liquid.accounting.domain.ReceivableSummaryEntity;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.core.security.SecurityContext;
import liquid.order.domain.OrderEntity;
import liquid.order.domain.OrderEntity_;
import liquid.order.domain.OrderStatus;
import liquid.order.domain.ServiceItemEntity;
import liquid.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * User: tao
 * Date: 9/29/13
 * Time: 8:03 PM
 */
@Service
public class OrderServiceImpl extends AbstractBaseOrderService<OrderEntity, OrderRepository> implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private ServiceItemServiceImpl serviceItemService;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Transactional("transactionManager")
    public void doSaveBefore(OrderEntity order) { }

    @Transactional(value = "transactionManager")
    public OrderEntity complete(Long orderId) {
        OrderEntity order = find(orderId);
        order.setStatus(OrderStatus.COMPLETED.getValue());
        order = save(order);
        return order;
    }

    public Page<OrderEntity> findByCreateUser(String username, Pageable pageable) {
        return repository.findByCreatedBy(username, pageable);
    }

    public Page<OrderEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<OrderEntity> findByStatus(Integer status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(value = "transactionManager")
    public OrderEntity find(Long id) {
        OrderEntity order = repository.findOne(id);
        // Initialize one to many children
        order.getServiceItems().size();
        return order;
    }

    /**
     * Criteria Query for order.
     *
     * @param orderNo
     * @param customerName
     * @param username
     * @param pageable
     * @return
     */
    public Page<OrderEntity> findAll(final String orderNo, final String customerName, final String username, final Pageable pageable) {
        List<Specification<OrderEntity>> specList = new ArrayList<>();

        if (null != orderNo) {
            Specification<OrderEntity> orderNoSpec = new Specification<OrderEntity>() {
                @Override
                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(OrderEntity_.orderNo), "%" + orderNo + "%");
                }
            };
            specList.add(orderNoSpec);
        }

//        if (null != customerName) {
//            Specification<OrderEntity> customerNameSpec = new Specification<OrderEntity>() {
//                @Override
//                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
//                    return builder.like(root.get(OrderEntity_.customer).get(CustomerEntity_.name), "%" + customerName + "%");
//                }
//            };
//            specList.add(customerNameSpec);
//        }

        if (null != username) {
            Specification<OrderEntity> usernameSpec = new Specification<OrderEntity>() {
                @Override
                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(OrderEntity_.createdBy), username);
                }
            };
        }

        if (specList.size() > 0) {
            Specifications<OrderEntity> specifications = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specifications.and(specList.get(i));
            }
            return repository.findAll(specifications, pageable);
        }

        return repository.findAll(pageable);
    }

    public Page<OrderEntity> findByCustomerId(Long customerId, String createdBy, Pageable pageable) {
        return repository.findByCustomerIdAndCreatedBy(customerId, createdBy, pageable);
    }

    public Page<OrderEntity> findByOrderNoLike(String orderNo, Pageable pageable) {
        return repository.findByOrderNoLike("%" + orderNo + "%", pageable);
    }

    @Transactional(value = "transactionManager")
    @Override
    public OrderEntity saveOrder(OrderEntity order) {
        List<ServiceItemEntity> serviceItemList = order.getServiceItems();
        Iterator<ServiceItemEntity> serviceItemIterator = serviceItemList.iterator();
        while (serviceItemIterator.hasNext()) {
            ServiceItemEntity serviceItem = serviceItemIterator.next();
            if (serviceItem.getQuotation() == null) serviceItemIterator.remove();
            else serviceItem.setOrder(order);
        }
        order.getRailway().setOrder(order);

        order = save(order);

        ReceivableSummaryEntity receivableSummary = new ReceivableSummaryEntity();
        receivableSummary.setCny(order.getTotalCny());
        receivableSummary.setUsd(order.getTotalUsd());
        receivableSummary.setOrder(order);
        order.setId(order.getId());
        // TODO: workaround
        receivableSummary.setId(null);
        receivableSummaryService.save(receivableSummary);

        return order;
    }

    @Override
    public OrderEntity submitOrder(OrderEntity order) {
        // set role
        order.setCreateRole(SecurityContext.getInstance().getRole());

        // compute order no.
        order.setOrderNo(computeOrderNo(order.getCreateRole(), order.getServiceType().getCode()));
        OrderEntity orderEntity = saveOrder(order);

        return orderEntity;
    }

    public Iterable<OrderEntity> findByOrderNoLike(String orderNo) {
        return repository.findByOrderNoLike("%" + orderNo + "%");
    }
}
