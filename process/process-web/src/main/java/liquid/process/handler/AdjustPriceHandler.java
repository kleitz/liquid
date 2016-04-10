package liquid.process.handler;

import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.service.ReceivableSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Tao Ma on 6/8/15.
 */
@Component
public class AdjustPriceHandler extends AbstractOrderPriceHandler {
    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Override
    public void preComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        ReceivableSummary receivableSummaryEntity = receivableSummaryService.findByOrderId(orderId);
        variableMap.put("salesPrice", "CNY: " + receivableSummaryEntity.getCny() + "; USD: " + receivableSummaryEntity.getUsd());
    }
}
