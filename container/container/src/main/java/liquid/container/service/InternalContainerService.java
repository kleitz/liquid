package liquid.container.service;

import liquid.container.domain.ContainerEntity;
import liquid.container.domain.ExcelFileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tao Ma on 4/9/15.
 */
public interface InternalContainerService extends ContainerService {
    Iterable<ContainerEntity> findByBicCodeLike(String bicCode);

    List<ContainerEntity> findBicCode(String bicCode);

    Page<ContainerEntity> findByBicCodeLike(String bicCode, Pageable pageable);

    void writeToFile(String fileName, byte[] bytes) throws IOException;

    ExcelFileInfo[] readMetadata() throws IOException;

    void importExcel(String fileName) throws IOException;
}
