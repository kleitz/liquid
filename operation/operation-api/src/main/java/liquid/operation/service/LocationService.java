package liquid.operation.service;

import liquid.operation.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface LocationService {
    List<Location> findYards();

    List<Location> findByTypeId(Byte typeId);

    Location find(Long id);

    Location findByName(String name);

    Location findByTypeAndName(Byte typeId, String name);
}
