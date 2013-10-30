package liquid.service;

import liquid.metadata.ContainerStatus;
import liquid.persistence.domain.Container;
import liquid.persistence.domain.Location;
import liquid.persistence.repository.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 1:11 AM
 */
@Service
public class ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private LocationService locationService;

    public Iterable<Container> findAll() {
        return containerRepository.findAll();
    }

    public Iterable<Container> findAllInStock() {
        return containerRepository.findByStatus(ContainerStatus.IN_STOCK.getValue());
    }

    public Container find(long id) {
        Container container = containerRepository.findOne(id);
        container.setYardId(container.getYard().getId());
        return container;
    }

    public void save(Container container) {
        if (null == container.getYard()) {
            Location yard = locationService.find(container.getYardId());
            container.setYard(yard);
        }
        containerRepository.save(container);
    }
}
