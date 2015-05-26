package liquid.accounting.service;

import liquid.accounting.domain.IncomeEntity;
import liquid.accounting.domain.IncomeType;
import liquid.accounting.domain.ReceivableSummary;
import liquid.accounting.repository.IncomeRepository;
import liquid.order.domain.Order;
import liquid.order.service.OrderService;
import liquid.process.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *  
 * User: tao
 * Date: 10/30/13
 * Time: 1:02 PM
 */
@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Autowired
    private OrderService orderService;

    public List<IncomeEntity> findByTaskId(String taskId) {
        return incomeRepository.findByTaskId(taskId);
    }

    public IncomeEntity addIncome(IncomeEntity income, String uid) {
        income.setCreatedBy(uid);
        income.setCreatedAt(new Date());
        income.setUpdatedBy(uid);
        income.setUpdatedAt(new Date());
        return incomeRepository.save(income);
    }

    public IncomeEntity addIncome(Order order, String uid) {
        ReceivableSummary receivableSummaryEntity = receivableSummaryService.findByOrderId(order.getId());

        IncomeEntity income = new IncomeEntity();
        income.setOrder(order);
        income.setType(IncomeType.ORDER.getType());
        income.setAmount(receivableSummaryEntity.getCny().longValue());
        income.setComment("Order");
        return addIncome(income, uid);
    }

    public IncomeEntity addIncome(String taskId, IncomeEntity income, String uid) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);

        income.setOrder(order);
        income.setTaskId(taskId);
        return addIncome(income, uid);
    }

    public void delIncome(long id) {
        incomeRepository.delete(id);
    }

    public long total(Iterable<IncomeEntity> incomes) {
        long total = 0L;
        for (IncomeEntity income : incomes) {
            total += income.getAmount();
        }
        return total;
    }
}
