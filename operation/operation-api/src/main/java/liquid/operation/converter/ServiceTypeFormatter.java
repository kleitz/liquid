package liquid.operation.converter;

import liquid.operation.domain.ServiceType;
import liquid.operation.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/20/15.
 */
@Component
public class ServiceTypeFormatter implements Formatter<ServiceType> {

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Override
    public ServiceType parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return serviceTypeService.find(Long.valueOf(text));
    }

    @Override
    public String print(ServiceType object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
