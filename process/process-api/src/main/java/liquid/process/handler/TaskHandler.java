package liquid.process.handler;

import liquid.process.domain.Task;
import org.springframework.ui.Model;

/**
 * Created by Tao Ma on 4/16/15.
 */
public interface TaskHandler {
    String getDefinitionKey();

    void setDefinitionKey(String definitionKey);

    // FIXME - this is temporary solution for refactor.
    boolean isRedirect();

    void init(Task task, Model model);

    String locateTemplate(String definitionKey);

    void complete(String taskId);
}
