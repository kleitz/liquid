package liquid.purchase.facade;

import liquid.facade.Facade;
import liquid.order.facade.OrderFacade;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.purchase.persistence.domain.ChargeEntity;
import liquid.purchase.service.ChargeService;
import liquid.purchase.web.domain.Charge;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.util.DateUtil;
import liquid.web.domain.EnhancedPageImpl;
import liquid.web.domain.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 6/9/14.
 */
@Service
public class ChargeFacade implements Facade<Charge, ChargeEntity> {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private OrderFacade orderFacade;

    public ChargeEntity save(Charge charge) {
        ChargeEntity entity = convert(charge);
        return chargeService.save(entity);
    }

    @Override
    public ChargeEntity convert(Charge charge) {
        ChargeEntity entity = new ChargeEntity();
        entity.setId(charge.getId());
        if (null != charge.getShipmentId())
            entity.setShipment(ShipmentEntity.newInstance(ShipmentEntity.class, charge.getShipmentId()));
        if (null != charge.getLegId())
            entity.setLeg(LegEntity.newInstance(LegEntity.class, charge.getLegId()));
        entity.setServiceSubtype(ServiceSubtypeEntity.newInstance(ServiceSubtypeEntity.class, charge.getServiceSubtypeId()));
        entity.setSp(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, charge.getServiceProviderId()));
        entity.setWay(charge.getWay());
        entity.setCurrency(charge.getCurrency());
        entity.setUnitPrice(charge.getUnitPrice());
        entity.setTaskId(charge.getTaskId());
        entity.setComment(charge.getComment());
        return entity;
    }

    public Page<Charge> findAll(SearchBarForm searchBar, Pageable pageable) {
        List<Charge> chargeList = new ArrayList<>();
        Charge sum = new Charge();

        Page<ChargeEntity> entityPage = null;
        if ("sp".equals(searchBar.getType())) {
            entityPage = chargeService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), null, searchBar.getId(), pageable);
        } else if ("order".equals(searchBar.getType())) {
            entityPage = chargeService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), searchBar.getId(), null, pageable);
        } else {
            entityPage = chargeService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), null, null, pageable);
        }
        for (ChargeEntity entity : entityPage) {
            Charge charge = convert(entity);
            orderFacade.convert(entity.getOrder(), charge);
            chargeList.add(charge);

            sum.setContainerQuantity(sum.getContainerQuantity() + charge.getContainerQuantity());
            sum.setTotalPrice(sum.getTotalPrice() + charge.getTotalPrice());
        }
        return new EnhancedPageImpl<Charge>(chargeList, pageable, entityPage.getTotalElements(), sum);
    }

    @Override
    public Charge convert(ChargeEntity entity) {
        Charge charge = new Charge();
        charge.setId(entity.getId());
        charge.setTaskId(entity.getTaskId());

        charge.setShipmentId(null == entity.getShipment() ? null : entity.getShipment().getId());
        charge.setLegId(null == entity.getLeg() ? null : entity.getLeg().getId());
        charge.setServiceSubtypeId(entity.getServiceSubtype().getId());
        charge.setServiceProviderId(entity.getSp().getId());
        charge.setServiceProviderName(entity.getSp().getName());
        charge.setWay(entity.getWay());
        charge.setCurrency(entity.getCurrency());
        charge.setUnitPrice(entity.getUnitPrice());
        charge.setTotalPrice(entity.getTotalPrice());
        return charge;
    }
}
