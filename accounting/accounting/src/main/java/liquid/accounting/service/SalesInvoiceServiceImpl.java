package liquid.accounting.service;

import liquid.accounting.domain.SalesInvoice;
import liquid.accounting.domain.SalesInvoice_;
import liquid.accounting.repository.SalesInvoiceRepository;
import liquid.core.service.AbstractService;
import liquid.operation.domain.Customer_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class SalesInvoiceServiceImpl extends AbstractService<SalesInvoice, SalesInvoiceRepository> implements InternalInvoiceService {
    @Override
    public void doSaveBefore(SalesInvoice entity) {}

    public Page<SalesInvoice> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<SalesInvoice> findAll(Date start, Date end, Long customerId, Pageable pageable) {
        Specification<SalesInvoice> dateSpec = new Specification<SalesInvoice>() {
            @Override
            public Predicate toPredicate(Root<SalesInvoice> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(SalesInvoice_.issuedAt), start, end);
            }
        };
        Specifications<SalesInvoice> specifications = where(dateSpec);

        if (null != customerId) {
            Specification<SalesInvoice> buyerSpec = new Specification<SalesInvoice>() {
                @Override
                public Predicate toPredicate(Root<SalesInvoice> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(SalesInvoice_.customer).get(Customer_.id), customerId);
                }
            };
            specifications.and(buyerSpec);
        }

        return repository.findAll(specifications, pageable);
    }

    public List<SalesInvoice> findByCustomerId(Long buyerId) {
        return repository.findByCustomerId(buyerId);
    }

    @Override
    public SalesInvoice update(SalesInvoice salesInvoice) {
        return null;
    }
}
