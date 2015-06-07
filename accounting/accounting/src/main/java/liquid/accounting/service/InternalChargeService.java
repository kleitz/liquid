package liquid.accounting.service;

import liquid.accounting.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InternalChargeService extends ChargeService {
    Iterable<Charge> findByLegId(long legId);

    Page<Charge> findAll(Pageable pageable);

    Page<Charge> findAll(final String orderNo, final String spName, final Pageable pageable);

    Page<Charge> findAll(final Date start, final Date end, final Long orderId, final Long spId, final Pageable pageable);
}
