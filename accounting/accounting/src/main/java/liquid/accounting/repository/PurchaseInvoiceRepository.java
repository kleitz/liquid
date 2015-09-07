package liquid.accounting.repository;

import liquid.accounting.domain.PurchaseInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 9/7/15.
 */
public interface PurchaseInvoiceRepository extends CrudRepository<PurchaseInvoice, Long> {
    Page<PurchaseInvoice> findAll(Pageable pageable);

    Page<PurchaseInvoice> findAll(Specification<PurchaseInvoice> specification, Pageable pageable);
}
