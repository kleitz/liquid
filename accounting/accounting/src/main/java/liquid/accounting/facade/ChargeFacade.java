package liquid.accounting.facade;

import liquid.accounting.domain.ChargeEntity;
import liquid.accounting.model.Charge;
import liquid.accounting.service.InternalChargeService;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceSubtype;
import liquid.transport.domain.LegEntity;
import liquid.transport.domain.ShipmentEntity;
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
public class ChargeFacade  {
    @Autowired
    private InternalChargeService chargeService;

    public ChargeEntity save(Charge charge) {
        ChargeEntity entity = convert(charge);
        return chargeService.save(entity);
    }

    public ChargeEntity convert(Charge charge) {
        ChargeEntity entity = new ChargeEntity();
        entity.setId(charge.getId());
        if (null != charge.getShipmentId())
            entity.setShipment(ShipmentEntity.newInstance(ShipmentEntity.class, charge.getShipmentId()));
        if (null != charge.getLegId())
            entity.setLeg(LegEntity.newInstance(LegEntity.class, charge.getLegId()));
        entity.setServiceSubtype(ServiceSubtype.newInstance(ServiceSubtype.class, charge.getServiceSubtypeId()));
        entity.setSp(ServiceProvider.newInstance(ServiceProvider.class, charge.getServiceProviderId()));
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
            chargeList.add(charge);

            sum.setContainerQuantity(sum.getContainerQuantity() + charge.getContainerQuantity());

            if (charge.getCurrency() == 0) {
                sum.setTotalCny(sum.getTotalCny().add(charge.getTotalPrice()));
            } else if (charge.getCurrency() == 1) {
                sum.setTotalUsd(sum.getTotalUsd().add(charge.getTotalPrice()));
            } else {

            }
        }
        return new EnhancedPageImpl<Charge>(chargeList, pageable, entityPage.getTotalElements(), sum);
    }

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
