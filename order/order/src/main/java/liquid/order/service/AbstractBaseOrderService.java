package liquid.order.service;

import liquid.core.domain.BaseUpdateEntity;
import liquid.service.AbstractService;
import liquid.service.SequenceService;
import liquid.service.ServiceTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;

/**
 * Created by redbrick9 on 4/26/14.
 */
public abstract class AbstractBaseOrderService<E extends BaseUpdateEntity, R extends CrudRepository<E, Long>> extends AbstractService<E, R> {

    @Autowired
    protected ServiceTypeServiceImpl serviceTypeService;

    @Autowired
    protected SequenceService sequenceService;


    public String computeOrderNo(String role, String serviceCode) {
        String sequenceName = String.format("%1$s%2$s%3$s%4$ty",
                salesOrMarketing(role),
                "C", // TODO
                serviceCode,
                Calendar.getInstance());
        return String.format("%1$s%2$05d",
                sequenceName, sequenceService.getNextValue(sequenceName));
    }

    /**
     * @param role is like "ROLE_SALES" from LDAP.
     * @return
     */
    private String salesOrMarketing(String role) {
        return "ROLE_SALES".equals(role) ? "S" : "M";
    }
}
