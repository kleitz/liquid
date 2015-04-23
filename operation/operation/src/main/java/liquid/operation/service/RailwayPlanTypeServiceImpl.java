package liquid.operation.service;

import liquid.operation.domain.RailPlanTypeEntity;
import liquid.operation.repository.RailPlanTypeRepository;
import liquid.core.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Service
public class RailwayPlanTypeServiceImpl extends AbstractCachedService<RailPlanTypeEntity, RailPlanTypeRepository>
        implements RailwayPlanTypeService {
    @Override
    public void doSaveBefore(RailPlanTypeEntity entity) {}
}
