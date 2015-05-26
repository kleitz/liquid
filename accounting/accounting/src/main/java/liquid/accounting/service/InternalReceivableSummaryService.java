package liquid.accounting.service;

import liquid.accounting.domain.AccountingOperator;
import liquid.accounting.domain.AccountingType;
import liquid.accounting.domain.ReceivableSummary;
import liquid.core.domain.SumPage;
import liquid.core.model.SearchBarForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * Created by Tao Ma on 4/12/15.
 */
public interface InternalReceivableSummaryService extends ReceivableSummaryService {
    Page<ReceivableSummary> findAll(final Date start, final Date end, final Long orderId, final Long customerId, final Pageable pageable);

    // FIXME - TBG - To Be Generic
    SumPage<ReceivableSummary> findAll(final SearchBarForm searchBarForm, final Pageable pageable);

    void update(Long orderId, AccountingType type, AccountingOperator op, Long amountCny, Long amountUsd);

    void update(Long orderId, AccountingType type, Long totalCny, Long totalUsd);
}
