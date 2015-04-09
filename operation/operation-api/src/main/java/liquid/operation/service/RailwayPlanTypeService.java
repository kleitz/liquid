package liquid.operation.service;

import liquid.operation.domain.RailPlanTypeEntity;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface RailwayPlanTypeService {
    Iterable<RailPlanTypeEntity> findAll();

    RailPlanTypeEntity find(Long id);

    RailPlanTypeEntity save(RailPlanTypeEntity railPlanTypeEntity);
}
