package liquid.accounting.service;

import liquid.accounting.domain.ChargeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ChargeService {
    Iterable<ChargeEntity> getChargesByOrderId(long orderId);

    Iterable<ChargeEntity> findByOrderId(long orderId);

    Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole);

    Iterable<ChargeEntity> findByTaskId(String taskId);

    Page<ChargeEntity> findAll(final Date start, final Date end, final Long orderId, final Long spId, final Pageable pageable);

    BigDecimal total(Iterable<ChargeEntity> charges);

    void removeCharge(long chargeId);

    ChargeEntity find(long id);

    ChargeEntity save(ChargeEntity chargeEntity);
}
