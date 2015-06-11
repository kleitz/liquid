package liquid.process.handler;

import liquid.process.domain.Task;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 6/11/15.
 */
public class AbstractOrderPriceHandler extends AbstractTaskHandler {
    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {}

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {

    }

    @Override
    public String locateTemplate(String definitionKey) {
        return Constants.TASK_ROOT_DIR + "/order_price/init";
    }
}
