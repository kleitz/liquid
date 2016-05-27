package liquid.accounting.service;

import liquid.accounting.domain.*;
import liquid.accounting.model.Earning;
import liquid.accounting.repository.ReceivableSummaryObsoloteRepository;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import liquid.core.service.AbstractService;
import liquid.operation.domain.Customer_;
import liquid.order.domain.Order;
import liquid.order.domain.Order_;
import liquid.util.NumberUtils;
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class ReceivableSummaryServiceObsoloteImpl extends AbstractService<ReceivableSummaryObsolete, ReceivableSummaryObsoloteRepository>
        implements InternalReceivableSummaryObsoloteService {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public void doSaveBefore(ReceivableSummaryObsolete entity) {}

    public ReceivableSummaryObsolete findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        repository.deleteByOrderId(orderId);
    }

    @Transactional(value = "transactionManager")
    public void update(Long orderId, AccountingType type, AccountingOperator op, Long amountCny, Long amountUsd) {
        ReceivableSummaryObsolete entity = repository.findByOrderId(orderId);
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
        ReceivableSummaryObsolete entity = repository.findByOrderId(orderId);
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

    public Page<ReceivableSummaryObsolete> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ReceivableSummaryObsolete> findAll(final Date start, final Date end, final Long orderId, final Long customerId, final Pageable pageable) {
        Specification<ReceivableSummaryObsolete> dateRangeSpec = new Specification<ReceivableSummaryObsolete>() {
            @Override
            public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(ReceivableSummary_.createdAt), start, end);
            }
        };
        Specifications<ReceivableSummaryObsolete> specifications = Specifications.where(dateRangeSpec);

        if (null != orderId) {
            Specification<ReceivableSummaryObsolete> orderIdSpec = new Specification<ReceivableSummaryObsolete>() {
                @Override
                public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ReceivableSummaryObsolete_.order).get(Order_.id), orderId);
                }
            };
            specifications = specifications.and(orderIdSpec);
        }

        if (null != customerId) {
            Specification<ReceivableSummaryObsolete> customerIdSpec = new Specification<ReceivableSummaryObsolete>() {
                @Override
                public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    return cb.equal(root.get(ReceivableSummaryObsolete_.order).get(Order_.customer).get(Customer_.id), customerId);
                }
            };
            specifications = specifications.and(customerIdSpec);
        }
        return repository.findAll(specifications, pageable);
    }

    @Override
    public SumPage<ReceivableSummaryObsolete> findAll(final SearchBarForm searchBarForm, final Pageable pageable) {
        Specification<ReceivableSummaryObsolete> dateRangeSpec = new Specification<ReceivableSummaryObsolete>() {
            @Override
            public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get(ReceivableSummary_.createdAt), searchBarForm.getStartDate(), searchBarForm.getEndDate());
            }
        };
        Specifications<ReceivableSummaryObsolete> specifications = Specifications.where(dateRangeSpec);

        if ("order".equals(searchBarForm.getType())) {
            if (null != searchBarForm.getId()) {
                Specification<ReceivableSummaryObsolete> orderIdSpec = new Specification<ReceivableSummaryObsolete>() {
                    @Override
                    public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        return cb.equal(root.get(ReceivableSummaryObsolete_.order).get(Order_.id), searchBarForm.getId());
                    }
                };
                specifications = specifications.and(orderIdSpec);
            }
        } else if ("customer".equals(searchBarForm.getType())) {
            if (null != searchBarForm.getId()) {
                Specification<ReceivableSummaryObsolete> customerIdSpec = new Specification<ReceivableSummaryObsolete>() {
                    @Override
                    public Predicate toPredicate(Root<ReceivableSummaryObsolete> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        return cb.equal(root.get(ReceivableSummaryObsolete_.order).get(Order_.customer).get(Customer_.id), searchBarForm.getId());
                    }
                };
                specifications = specifications.and(customerIdSpec);
            }
        }
        Page<ReceivableSummaryObsolete> page = repository.findAll(specifications, pageable);
        return appendSum(page, pageable);
    }

    private SumPage<ReceivableSummaryObsolete> appendSum(Page<ReceivableSummaryObsolete> page, Pageable pageable) {
        ReceivableSummaryObsolete sum = new ReceivableSummaryObsolete();
        Order order = new Order();
        sum.setOrder(order);
        for (ReceivableSummaryObsolete entity : page) {
            sum.getOrder().setContainerQty(NumberUtils.valueOf(sum.getOrder().getContainerQty()) +
                    NumberUtils.valueOf(entity.getOrder().getContainerQty()));
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

        return new SumPage<ReceivableSummaryObsolete>(page, sum, pageable);
    }

    @Override
    public Earning calculateEarning(Long orderId) {
        Earning earning = new Earning();

        BigDecimal exchangeRate = exchangeRateService.getExchangeRate().getValue();

        ReceivableSummaryObsolete receivableSummaryEntity = findByOrderId(orderId);

        earning.setSalesPriceCny(receivableSummaryEntity.getCny());
        earning.setSalesPriceUsd(receivableSummaryEntity.getUsd());
        earning.setDistyPrice(receivableSummaryEntity.getOrder().getDistyCny());
        earning.setGrandTotal(receivableSummaryEntity.getOrder().getGrandTotal());
        earning.setGrossMargin(earning.getSalesPriceCny().add(earning.getSalesPriceUsd().multiply(exchangeRate)).subtract(receivableSummaryEntity.getOrder().getGrandTotal()));
        earning.setSalesProfit(receivableSummaryEntity.getCny().add(earning.getSalesPriceUsd().multiply(exchangeRate)).subtract(receivableSummaryEntity.getOrder().getDistyCny()));
        earning.setDistyProfit(earning.getDistyPrice().subtract(receivableSummaryEntity.getOrder().getGrandTotal()));
        return earning;
    }
}
