package liquid.accounting.repository;

import liquid.accounting.domain.SalesInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Tao Ma on 1/8/15.
 */
public interface SalesInvoiceRepository extends CrudRepository<SalesInvoice, Long> {
    Page<SalesInvoice> findAll(Pageable pageable);

    List<SalesInvoice> findByCustomerId(Long customerId);

    Page<SalesInvoice> findAll(Specification<SalesInvoice> specification, Pageable pageable);
}
