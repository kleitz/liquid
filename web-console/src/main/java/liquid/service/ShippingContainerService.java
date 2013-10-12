package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:36 AM
 */
@Service
public class ShippingContainerService {


    @Autowired
    private OrderService orderService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerRepository scRepository;

    @Autowired
    private RailContainerRepository rcRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ContainerRepository containerRepository;

    public void add(long routeId, ShippingContainer sc) {
        Route route = routeRepository.findOne(routeId);
        sc.setRoute(route);
        Container container = containerRepository.findOne(sc.getContainerId());
        sc.setContainer(container);
        scRepository.save(sc);
    }

    public void remove(long scId) {
        scRepository.delete(scId);
    }

    public Iterable<RailContainer> initialize(String taskId) {
        Order order = orderService.findByTaskId(taskId);
        Collection<RailContainer> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            for (RailContainer container : rcList) {
                if (container.getLoadingToc() != null) {
                    container.setLoadingTocStr(DateUtils.stringOf(container.getLoadingToc()));
                }
                if (container.getStationToa() != null) {
                    container.setStationToaStr(DateUtils.stringOf(container.getStationToa()));
                }
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtils.dayStrOf(container.getEts()));
                }
            }
            return rcList;
        }

        rcList = new ArrayList<RailContainer>();
        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<ShippingContainer> scList = scRepository.findByRoute(route);
            List<Leg> legList = legRepository.findByRouteAndTransMode(route, TransMode.RAIL.getType());
            if (legList.size() > 0) {
                for (ShippingContainer sc : scList) {
                    RailContainer rc = new RailContainer();
                    rc.setOrder(route.getPlanning().getOrder());
                    rc.setLeg(legList.get(0));
                    rc.setSc(sc);
                    rcList.add(rc);
                }
            }
        }

        return rcRepository.save(rcList);
    }

    public RailContainer findRailContainer(long containerId) {
        RailContainer railContainer = rcRepository.findOne(containerId);
        String defaultDateStr = DateUtils.stringOf(new Date());
        if (null == railContainer.getLoadingToc())
            railContainer.setLoadingTocStr(defaultDateStr);
        else
            railContainer.setLoadingTocStr(DateUtils.stringOf(railContainer.getLoadingToc()));

        if (null == railContainer.getStationToa())
            railContainer.setStationToaStr(defaultDateStr);
        else
            railContainer.setStationToaStr(DateUtils.stringOf(railContainer.getStationToa()));

        String defaultDayStr = DateUtils.dayStrOf(new Date());
        if (null == railContainer.getEts())
            railContainer.setEtsStr(defaultDayStr);
        else
            railContainer.setEtsStr(DateUtils.dayStrOf(railContainer.getStationToa()));
        return railContainer;
    }

    public void saveRailContainer(long containerId, RailContainer formBean) {
        RailContainer container = rcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            if (formBean.getLoadingTocStr() != null && formBean.getLoadingTocStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setLoadingToc(DateUtils.dateOf(formBean.getLoadingTocStr()));
                }
            }

            if (formBean.getStationToaStr() != null && formBean.getStationToaStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setStationToa(DateUtils.dateOf(formBean.getStationToaStr()));
                }
            }

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setEts(DateUtils.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getTransPlanNo() != null && formBean.getTransPlanNo().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setTransPlanNo(formBean.getTransPlanNo());
                }
            }
            rcRepository.save(containers);
        } else {
            if (formBean.getLoadingTocStr() != null && formBean.getLoadingTocStr().trim().length() > 0) {
                container.setLoadingToc(DateUtils.dateOf(formBean.getLoadingTocStr()));
            }
            if (formBean.getStationToaStr() != null && formBean.getStationToaStr().trim().length() > 0) {
                container.setStationToa(DateUtils.dateOf(formBean.getStationToaStr()));
            }
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtils.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getTransPlanNo() != null && formBean.getTransPlanNo().trim().length() > 0) {
                container.setTransPlanNo(formBean.getTransPlanNo());
            }
            rcRepository.save(container);
        }
    }
}