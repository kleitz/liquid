package liquid.accounting.repository;

import liquid.accounting.domain.PurchaseInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mat on 5/24/16.
 */
public interface PurchaseInvoiceRepository extends CrudRepository<PurchaseInvoice, Long>{
    Page<PurchaseInvoice> findAll(Pageable pageable);

    List<PurchaseInvoice> findByCustomerId(Long customerId);

    Page<PurchaseInvoice> findAll(Specification<PurchaseInvoice> specification, Pageable pageable);
}
