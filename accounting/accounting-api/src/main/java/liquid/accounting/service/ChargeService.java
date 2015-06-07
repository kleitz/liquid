package liquid.accounting.service;

import liquid.accounting.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ChargeService {
    Iterable<Charge> getChargesByOrderId(long orderId);

    Iterable<Charge> findByOrderId(long orderId);

    Iterable<Charge> findByOrderIdAndCreateRole(long orderId, String createRole);

    Iterable<Charge> findByTaskId(String taskId);

    Page<Charge> findAll(final Date start, final Date end, final Long orderId, final Long spId, final Pageable pageable);

    BigDecimal total(Iterable<Charge> charges);

    void removeCharge(long chargeId);

    Charge find(long id);

    Charge save(Charge chargeEntity);
}
