package liquid.accounting.service;

import liquid.accounting.domain.Payable;
import liquid.accounting.repository.PayableRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 6/7/15.
 */
@Service
public class PayableService {

    @Autowired
    private PayableRespository payableRespository;

    public Iterable<Payable> findByChargeId(Long chargeId) {
        return payableRespository.findByChargeId(chargeId);
    }
}
