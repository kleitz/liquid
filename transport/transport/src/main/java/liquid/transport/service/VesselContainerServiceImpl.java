package liquid.transport.service;

import liquid.service.AbstractService;
import liquid.transport.domain.VesselContainer;
import liquid.transport.repository.VesselContainerRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/20/15.
 */
@Service
public class VesselContainerServiceImpl extends AbstractService<VesselContainer, VesselContainerRepository>
        implements VesselContainerService {
    @Override
    public void doSaveBefore(VesselContainer entity) {}
}
