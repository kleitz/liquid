package liquid.transport.service;

import liquid.service.AbstractService;
import liquid.transport.domain.BargeContainerEntity;
import liquid.transport.repository.BargeContainerRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/20/15.
 */
@Service
public class BargeContainerServiceImpl extends AbstractService<BargeContainerEntity, BargeContainerRepository>
        implements BargeContainerService {

    @Override
    public void doSaveBefore(BargeContainerEntity entity) { }
}
