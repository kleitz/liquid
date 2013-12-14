package liquid.service;

import liquid.persistence.domain.BargeContainer;
import liquid.persistence.domain.Route;
import liquid.persistence.repository.BargeContainerRepository;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by tao on 12/14/13.
 */
@Service
public class BargeContainerService {

    @Autowired
    private BargeContainerRepository bargeContainerRepository;

    public Collection<BargeContainer> findByRoute(Route route) {
        Collection<BargeContainer> bargeContainers = bargeContainerRepository.findByRoute(route);

        for (BargeContainer bargeContainer : bargeContainers) {
            if (null != bargeContainer.getEts()) bargeContainer.setEtsStr(DateUtils.dayStrOf(bargeContainer.getEts()));
        }
        return bargeContainers;
    }
}
