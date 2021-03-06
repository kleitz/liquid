package liquid.process.handler;

import liquid.process.domain.Task;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Component
public class DefaultHandler extends AbstractTaskHandler {
    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {
    }

    @Override
    public boolean isRedirect() {
        return true;
    }

    @Override
    public void init(Task task, Model model) {
    }
}
