package liquid.accounting.service;

import liquid.accounting.domain.AccountingOperator;
import liquid.accounting.domain.AccountingType;
import liquid.accounting.domain.ReceivableSummaryEntity;
import liquid.accounting.domain.ReceivableSummaryEntity_;
import liquid.accounting.repository.ReceivableSummaryRepository;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.core.service.AbstractService;
import liquid.operation.domain.Customer_;
import liquid.order.domain.OrderEntity;
import liquid.order.domain.OrderEntity_;
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
import java.util.Date;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class ReceivableSummaryServiceImpl extends AbstractService<ReceivableSummaryEntity, ReceivableSummaryRepository>
        implements InternalReceivableSummaryService {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public void doSaveBefore(ReceivableSummaryEntity entity) {}

    public ReceivableSummaryEntity findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    @Transactional(value = "transactionManager")
    public void update(Long orderId, AccountingType type, AccountingOperator op, Long amountCny, Long amountUsd) {
        ReceivableSummaryEntity entity = repository.findByOrderId(orderId);
        switch (type) {
            case SETTLEMENT:
                switch (op) {
                    case PLUS:
                    case MINUS:
                        break;
                }
                break;
            case INVOICE:
                switch (op) {
                    case PLUS:
                        entity.setInvoicedCny(entity.getInvoicedCny() + amountCny);
                        entity.setInvoicedUsd(entity.getInvoicedUsd() + amountUsd);
                        break;
                    case MINUS:
                        entity.setInvoicedCny(entity.getInvoicedCny() - amountCny);
                        entity.setInvoicedUsd(entity.getInvoicedUsd() - amountUsd);
                }
                break;
            case PAYMENT:
                switch (op) {
                    case PLUS:
                        entity.setPaidCny(entity.getPaidCny() + amountCny);
                        entity.setPaidUsd(entity.getPaidUsd() + amountUsd);
                        break;
                    case MINUS:
                        entity.setPaidCny(entity.getPaidCny() - amountCny);
                        entity.setPaidUsd(entity.getPaidUsd() - amountUsd);
                        break;
                }
                break;
        }
        save(entity);
    }

    @Transactional(value = "transactionManager")
    public void update(Long orderId, AccountingType type, Long totalCny, Long totalUsd) {
        ReceivableSummaryEntity entity = repository.findByOrderId(orderId);
        switch (type) {
            case SETTLEMENT:
                break;
            case INVOICE:
                entity.setInvoicedCny(totalCny);
                entity.setInvoicedUsd(totalUsd);
                break;
            case PAYMENT:
                entity.setPaidCny(totalCny);
                entity.setPaidUsd(totalUsd);
                break;
        }
        save(entity);
    }

    public Page<ReceivableSummaryEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ReceivableSummaryEntity> findAll(final Date start, final Date end, final Long orderId, final Long customerId, final Pageable pageable) {
        Specification<ReceivableSummaryEntity> dateRangeSpec = new Specification<ReceivableSummaryEntity>() {
            @Override
            public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(ReceivableSummaryEntity_.createdAt), start, end);
            }
        };
        Specifications<ReceivableSummaryEntity> specifications = Specifications.where(dateRangeSpec);

        if (null != orderId) {
            Specification<ReceivableSummaryEntity> orderIdSpec = new Specification<ReceivableSummaryEntity>() {
                @Override
                public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ReceivableSummaryEntity_.order).get(OrderEntity_.id), orderId);
                }
            };
            specifications = specifications.and(orderIdSpec);
        }

        if (null != customerId) {
            Specification<ReceivableSummaryEntity> customerIdSpec = new Specification<ReceivableSummaryEntity>() {
                @Override
                public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ReceivableSummaryEntity_.order).get(OrderEntity_.customer).get(Customer_.id), customerId);
                }
            };
            specifications = specifications.and(customerIdSpec);
        }
        return repository.findAll(specifications, pageable);
    }

    @Override
    public SumPage<ReceivableSummaryEntity> findAll(final SearchBarForm searchBarForm, final Pageable pageable) {
        Specification<ReceivableSummaryEntity> dateRangeSpec = new Specification<ReceivableSummaryEntity>() {
            @Override
            public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(ReceivableSummaryEntity_.createdAt), searchBarForm.getStartDate(), searchBarForm.getEndDate());
            }
        };
        Specifications<ReceivableSummaryEntity> specifications = Specifications.where(dateRangeSpec);

        if ("order".equals(searchBarForm.getType())) {
            if (null != searchBarForm.getId()) {
                Specification<ReceivableSummaryEntity> orderIdSpec = new Specification<ReceivableSummaryEntity>() {
                    @Override
                    public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        return cb.equal(root.get(ReceivableSummaryEntity_.order).get(OrderEntity_.id), searchBarForm.getId());
                    }
                };
                specifications = specifications.and(orderIdSpec);
            }
        } else if ("customer".equals(searchBarForm.getType())) {
            if (null != searchBarForm.getId()) {
                Specification<ReceivableSummaryEntity> customerIdSpec = new Specification<ReceivableSummaryEntity>() {
                    @Override
                    public Predicate toPredicate(Root<ReceivableSummaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        return cb.equal(root.get(ReceivableSummaryEntity_.order).get(OrderEntity_.customer).get(Customer_.id), searchBarForm.getId());
                    }
                };
                specifications = specifications.and(customerIdSpec);
            }
        }
        Page<ReceivableSummaryEntity> page = repository.findAll(specifications, pageable);
        return appendSum(page, pageable);
    }

    private SumPage<ReceivableSummaryEntity> appendSum(Page<ReceivableSummaryEntity> page, Pageable pageable) {
        ReceivableSummaryEntity sum = new ReceivableSummaryEntity();
        OrderEntity order = new OrderEntity();
        sum.setOrder(order);
        for (ReceivableSummaryEntity entity : page) {
            sum.getOrder().setContainerQty(sum.getOrder().getContainerQty() + entity.getOrder().getContainerQty());
            sum.setCny(sum.getCny().add(entity.getCny()));
            sum.setUsd(sum.getUsd().add(entity.getUsd()));
            sum.setRemainingBalanceCny(sum.getRemainingBalanceCny() + entity.getRemainingBalanceCny());
            sum.setRemainingBalanceUsd(sum.getRemainingBalanceUsd() + entity.getRemainingBalanceUsd());
            sum.setPaidCny(sum.getPaidCny() + entity.getPaidCny());
            sum.setPaidUsd(sum.getPaidUsd() + entity.getPaidUsd());
            sum.setInvoicedCny(sum.getInvoicedCny() + entity.getInvoicedCny());
            sum.setInvoicedUsd(sum.getInvoicedUsd() + entity.getInvoicedUsd());

            sum.getOrder().setDistyCny(sum.getOrder().getDistyCny().add(entity.getOrder().getDistyCny()));
            sum.getOrder().setDistyUsd(sum.getOrder().getDistyUsd().add(entity.getOrder().getDistyUsd()));

            sum.getOrder().setGrandTotal(sum.getOrder().getGrandTotal().add(entity.getOrder().getGrandTotal()));
        }

        return new SumPage<ReceivableSummaryEntity>(page, sum, pageable);
    }
}
