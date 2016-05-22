package liquid.accounting.service;

import liquid.accounting.domain.Invoice;
import liquid.accounting.domain.Invoice_;
import liquid.accounting.repository.InvoiceRepository;
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
public class InvoiceServiceImpl extends AbstractService<Invoice, InvoiceRepository> implements InternalInvoiceService  {
    @Override
    public void doSaveBefore(Invoice entity) {}

    public Page<Invoice> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Invoice> findAll(Date start, Date end, Long customerId, Pageable pageable) {
        Specification<Invoice> dateSpec = new Specification<Invoice>() {
            @Override
            public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(Invoice_.issuedAt), start, end);
            }
        };
        Specifications<Invoice> specifications = where(dateSpec);

        if (null != customerId) {
            Specification<Invoice> buyerSpec = new Specification<Invoice>() {
                @Override
                public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get(Invoice_.customer).get(Customer_.id), customerId);
                }
            };
            specifications.and(buyerSpec);
        }

        return repository.findAll(specifications, pageable);
    }

    public List<Invoice> findByCustomerId(Long buyerId) {
        return repository.findByCustomerId(buyerId);
    }

    @Override
    public Invoice update(Invoice invoice) {
        return null;
    }
}
