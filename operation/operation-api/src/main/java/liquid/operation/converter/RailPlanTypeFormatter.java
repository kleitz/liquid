package liquid.operation.converter;

import liquid.operation.domain.RailPlanType;
import liquid.operation.service.RailwayPlanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/21/15.
 */
@Component
public class RailPlanTypeFormatter implements Formatter<RailPlanType> {

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @Override
    public RailPlanType parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return railwayPlanTypeService.find(Long.valueOf(text));
    }

    @Override
    public String print(RailPlanType object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
