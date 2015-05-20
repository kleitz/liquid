package liquid.container.converter;

import liquid.container.domain.ContainerSubtype;
import liquid.container.service.ContainerSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/20/15.
 */
@Component
public class ContainerSubtypeFormatter implements Formatter<ContainerSubtype> {

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Override
    public ContainerSubtype parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return containerSubtypeService.find(Long.valueOf(text));
    }

    @Override
    public String print(ContainerSubtype object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
