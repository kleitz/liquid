package liquid.operation.service;

import liquid.operation.domain.RailPlanType;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface RailwayPlanTypeService {
    Iterable<RailPlanType> findAll();

    RailPlanType find(Long id);

    RailPlanType save(RailPlanType railPlanTypeEntity);
}
