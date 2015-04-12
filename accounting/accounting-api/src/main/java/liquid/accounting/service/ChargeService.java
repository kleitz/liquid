package liquid.accounting.service;

import liquid.accounting.domain.ChargeEntity;

import java.math.BigDecimal;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface ChargeService {
    Iterable<ChargeEntity> getChargesByOrderId(long orderId);

    Iterable<ChargeEntity> findByOrderId(long orderId);

    Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole);

    Iterable<ChargeEntity> findByTaskId(String taskId);

    BigDecimal total(Iterable<ChargeEntity> charges);

    void removeCharge(long chargeId);

    ChargeEntity find(long id);

    ChargeEntity save(ChargeEntity chargeEntity);
}
