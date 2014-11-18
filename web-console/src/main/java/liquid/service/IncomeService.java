package liquid.service;

import liquid.accounting.persistence.domain.IncomeEntity;
import liquid.accounting.persistence.repository.IncomeRepository;
import liquid.metadata.IncomeType;
import liquid.order.persistence.domain.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
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

    public List<IncomeEntity> findByTaskId(String taskId) {
        return incomeRepository.findByTaskId(taskId);
    }

    public IncomeEntity addIncome(IncomeEntity income, String uid) {
        income.setCreateUser(uid);
        income.setCreateTime(new Date());
        income.setUpdateUser(uid);
        income.setUpdateTime(new Date());
        return incomeRepository.save(income);
    }

    public IncomeEntity addIncome(OrderEntity order, String uid) {
        IncomeEntity income = new IncomeEntity();
        income.setOrder(order);
        income.setType(IncomeType.ORDER.getType());
        income.setAmount(order.getReceivableSummary().getCny());
        income.setComment("Order");
        return addIncome(income, uid);
    }

    public IncomeEntity addIncome(String taskId, IncomeEntity income, String uid) {
        OrderEntity order = taskService.findOrderByTaskId(taskId);

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
