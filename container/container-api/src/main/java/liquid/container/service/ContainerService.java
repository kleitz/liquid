package liquid.container.service;

import liquid.container.domain.ContainerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface ContainerService {
    ContainerEntity find(Long id);

    ContainerEntity save(ContainerEntity containerEntity);

    Iterable<ContainerEntity> save(List<ContainerEntity> containerEntities);

    // FIXME - Page should not occur here.
    Page<ContainerEntity> findAll(final long containerSubtypeId, final long ownerId, final long yardId, final Pageable pageable);
}
