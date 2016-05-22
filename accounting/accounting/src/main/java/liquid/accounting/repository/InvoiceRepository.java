package liquid.accounting.repository;

import liquid.accounting.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Tao Ma on 1/8/15.
 */
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    Page<Invoice> findAll(Pageable pageable);

    List<Invoice> findByCustomerId(Long customerId);

    Page<Invoice> findAll(Specification<Invoice> specification, Pageable pageable);
}
