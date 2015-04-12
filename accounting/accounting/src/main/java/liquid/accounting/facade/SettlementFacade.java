package liquid.accounting.facade;

import liquid.accounting.domain.SettlementEntity;
import liquid.accounting.model.Settlement;
import liquid.accounting.model.Statement;
import liquid.accounting.service.InternalSettlementService;
import liquid.accounting.service.SettlementServiceImpl;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class SettlementFacade implements InternalSettlementService {

    @Autowired
    private SettlementServiceImpl settlementService;

    @Autowired
    private OrderService orderService;

    public Statement<Settlement> findByOrderId(Long orderId) {
        Statement<Settlement> statement = new Statement<>();
        List<Settlement> settlementList = new ArrayList<>();
        Settlement total = new Settlement();

        Iterable<SettlementEntity> settlementEntities = settlementService.findByOrderId(orderId);
        for (SettlementEntity settlementEntity : settlementEntities) {
            Settlement settlement = new Settlement();
            settlement.setId(settlementEntity.getId());
            settlement.setOrderId(settlementEntity.getOrder().getId());
            settlement.setOrderNo(settlementEntity.getOrder().getOrderNo());
            settlement.setCny(settlementEntity.getCny());
            settlement.setUsd(settlementEntity.getUsd());
            settlement.setSettledAt(DateUtil.dayStrOf(settlementEntity.getSettledAt()));
            settlementList.add(settlement);
            total.setCny(total.getCny() + settlement.getCny());
            total.setUsd(total.getUsd() + settlement.getUsd());
        }
        statement.setContent(settlementList);
        statement.setTotal(total);

        return statement;
    }

    public Settlement save(Settlement settlement) {
        OrderEntity orderEntity = orderService.find(settlement.getOrderId());

        SettlementEntity settlementEntity = new SettlementEntity();
        settlementEntity.setId(settlement.getId());
        settlementEntity.setOrder(orderEntity);
        settlementEntity.setCny(settlement.getCny());
        settlementEntity.setUsd(settlement.getUsd());
        settlementEntity.setSettledAt(DateUtil.dayOf(settlement.getSettledAt()));
        settlementService.save(settlementEntity);
        return settlement;
    }
}
