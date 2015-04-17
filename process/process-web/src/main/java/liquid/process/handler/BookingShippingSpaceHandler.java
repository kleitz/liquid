package liquid.process.handler;

import liquid.process.domain.Task;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * Created by Tao Ma on 4/17/15.
 */
public class BookingShippingSpaceHandler extends AbstractTaskHandler {
    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {

    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void init(Task task, Model model) {

    }
}
