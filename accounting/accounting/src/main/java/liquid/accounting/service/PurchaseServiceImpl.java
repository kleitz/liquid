package liquid.accounting.service;

import liquid.accounting.repository.ChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 9/27/14.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public void deleteByLegId(Long legId) {
        chargeRepository.deleteByLegId(legId);
    }
}
