package liquid.shipping.service;

import liquid.finance.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.LegRepository;
import liquid.shipping.persistence.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by redbrick9 on 9/4/14.
 */
@Service
public class LegService extends AbstractService<LegEntity, LegRepository> {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(LegEntity legEntity) {}

    public LegEntity find(Long id) {
        return repository.findOne(id);
    }

    public List<LegEntity> findByRouteAndTransMode(RouteEntity route, int transMode) {
        return repository.findByRouteAndTransMode(route, transMode);
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        purchaseService.deleteByLegId(id);

        LegEntity leg = repository.findOne(id);
        RouteEntity route = leg.getRoute();
        route.getLegs().remove(leg);
        routeRepository.save(route);
    }
}