package liquid.accounting.service;

import liquid.accounting.domain.IncomeEntity;

import java.util.List;

/**
 * Created by Tao Ma on 6/8/15.
 */
public interface IncomeService {
    List<IncomeEntity> findByTaskId(String taskId);

    long total(Iterable<IncomeEntity> incomes);
}
