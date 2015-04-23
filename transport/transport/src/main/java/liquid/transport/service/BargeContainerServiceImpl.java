package liquid.transport.service;

import liquid.core.service.AbstractService;
import liquid.transport.domain.BargeContainer;
import liquid.transport.repository.BargeContainerRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/20/15.
 */
@Service
public class BargeContainerServiceImpl extends AbstractService<BargeContainer, BargeContainerRepository>
        implements BargeContainerService {

    @Override
    public void doSaveBefore(BargeContainer entity) { }
}
