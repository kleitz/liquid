package liquid.service;

import liquid.core.domain.RailPlanTypeEntity;
import liquid.core.repository.RailPlanTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Service
public class RailwayPlanTypeService extends AbstractCachedService<RailPlanTypeEntity, RailPlanTypeRepository> {
    @Override
    public void doSaveBefore(RailPlanTypeEntity entity) {}
}
