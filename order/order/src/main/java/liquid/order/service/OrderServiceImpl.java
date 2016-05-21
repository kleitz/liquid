package liquid.order.service;

import liquid.accounting.domain.Revenue;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.accounting.service.RevenueService;
import liquid.core.security.SecurityContext;
import liquid.operation.domain.Customer_;
import liquid.order.domain.Order;
import liquid.order.domain.OrderStatus;
import liquid.order.domain.Order_;
import liquid.order.domain.ServiceItem;
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
public class OrderServiceImpl extends AbstractBaseOrderService<Order, OrderRepository> implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private ServiceItemServiceImpl serviceItemService;

    @Autowired
    private RevenueService revenueService;

    @Deprecated
    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Transactional("transactionManager")
    public void doSaveBefore(Order order) { }

    @Transactional(value = "transactionManager")
    public Order complete(Long orderId) {
        Order order = find(orderId);
        order.setStatus(OrderStatus.COMPLETED.getValue());
        order = save(order);
        return order;
    }

    public Page<Order> findByCreateUser(String username, Pageable pageable) {
        return repository.findByCreatedBy(username, pageable);
    }

    public Page<Order> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Order> findByStatus(Integer status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(value = "transactionManager")
    public Order find(Long id) {
        Order order = repository.findOne(id);
        // Initialize one to many children
        order.getServiceItems().size();
        order.getContainers().size();
        return order;
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
    @Override
    public Page<Order> findAll(final Long id, final Long customerId, final String username, final Boolean isDiscarded, final Pageable pageable) {
        List<Specification<Order>> specList = new ArrayList<>();

        if (null != id) {
            Specification<Order> orderNoSpec = new Specification<Order>() {
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(Order_.id), id);
                }
            };
            specList.add(orderNoSpec);
        }

        if (null != customerId) {
            Specification<Order> customerNameSpec = new Specification<Order>() {
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(Order_.customer).get(Customer_.id), customerId);
                }
            };
            specList.add(customerNameSpec);
        }

        if (null != username) {
            Specification<Order> usernameSpec = new Specification<Order>() {
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(Order_.createdBy), username);
                }
            };
        }

        Specification<Order> statusSpec = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                if(isDiscarded) {
                    return builder.equal(root.get(Order_.status), 3);
                } else {
                    return builder.or(builder.equal(root.get(Order_.status), 1), builder.equal(root.get(Order_.status), 2), builder.equal(root.get(Order_.status), 8));
                }
            }
        };
        specList.add(statusSpec);

        Specifications<Order> specifications = where(specList.get(0));
        for (int i = 1; i < specList.size(); i++) {
            specifications.and(specList.get(i));
        }
        return repository.findAll(specifications, pageable);
    }

    public Page<Order> findByCustomerId(Long customerId, String createdBy, Pageable pageable) {
        return repository.findByCustomerIdAndCreatedBy(customerId, createdBy, pageable);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    public Page<Order> findByOrderNoLike(String orderNo, Pageable pageable) {
        return repository.findByOrderNoLike("%" + orderNo + "%", pageable);
    }

    @Transactional(value = "transactionManager")
    @Override
    public Order saveOrder(Order order) {
        List<ServiceItem> serviceItemList = order.getServiceItems();
        Iterator<ServiceItem> serviceItemIterator = serviceItemList.iterator();
        while (serviceItemIterator.hasNext()) {
            ServiceItem serviceItem = serviceItemIterator.next();
            if (serviceItem.getQuotation() == null) serviceItemIterator.remove();
        }
        order = save(order);

        Revenue revenue = revenueService.findByCustomerId(order.getCustomer().getId());
        if(null == revenue) {
            revenue = new Revenue();
            revenue.setCustomer(order.getCustomer());
        }
        revenue.setTotalCny(revenue.getTotalCny().add(order.getTotalCny()));
        revenue.setTotalUsd(revenue.getTotalUsd().add(order.getTotalUsd()));
        revenueService.save(revenue);

        return order;
    }

    @Transactional(value = "transactionManager")
    @Override
    public Order addItem(Long id, ServiceItem item) {
        Order order = find(id);
        order.getServiceItems().add(item);

        Revenue revenue = revenueService.findByCustomerId(order.getCustomer().getId());
        switch (item.getCurrency()) {
            case CNY:
                order.setTotalCny(order.getTotalCny().add(item.getQuotation()));
                revenue.setTotalCny(revenue.getTotalCny().add(item.getQuotation()));
                break;
            case USD:
                order.setTotalUsd(order.getTotalUsd().add(item.getQuotation()));
                revenue.setTotalUsd(revenue.getTotalUsd().add(item.getQuotation()));
                break;
            default:
                break;
        }
        save(order);
        revenueService.save(revenue);

        return order;
    }

    @Transactional(value = "transactionManager")
    @Override
    public Order voidItem(Long id, Long itemId) {
        Order order = find(id);
        ServiceItem item = order.getServiceItems().stream().filter(i -> itemId == i.getId()).
                findFirst().get();
        item.setStatus(1);
        Revenue revenue = revenueService.findByCustomerId(order.getCustomer().getId());
        switch (item.getCurrency()) {
            case CNY:
                order.setTotalCny(order.getTotalCny().subtract(item.getQuotation()));
                revenue.setTotalCny(revenue.getTotalCny().subtract(item.getQuotation()));
                break;
            case USD:
                order.setTotalUsd(order.getTotalUsd().subtract(item.getQuotation()));
                revenue.setTotalUsd(revenue.getTotalUsd().subtract(item.getQuotation()));
                break;
            default:
                break;
        }
        save(order);
        revenueService.save(revenue);
        return order;
    }

    @Transactional(value = "transactionManager")
    @Override
    public void delete(Long id) {
        receivableSummaryService.deleteByOrderId(id);
        repository.delete(id);
    }

    @Override
    public Order submitOrder(Order order) {
        // set role
        order.setCreateRole(SecurityContext.getInstance().getRole());

        // compute order no.
        order.setOrderNo(computeOrderNo(order.getCreateRole(), order.getServiceType().getCode()));
        Order orderEntity = saveOrder(order);

        return orderEntity;
    }

    @Override
    public Order discard(Long id) {
        Order order = find(id);
        order.setStatus(OrderStatus.DISCARDED.getValue());
        return saveOrder(order);
    }

    public Iterable<Order> findByOrderNoLike(String orderNo) {
        return repository.findByOrderNoLike("%" + orderNo + "%");
    }
}
