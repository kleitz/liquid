package liquid.transport.service;

import liquid.transport.domain.LegEntity;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface LegService {
    LegEntity find(Long id);

    LegEntity save(LegEntity legEntity);

    void delete(Long id);
}
