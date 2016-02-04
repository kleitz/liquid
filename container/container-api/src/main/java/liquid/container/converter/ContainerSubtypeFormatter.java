package liquid.container.converter;

import liquid.container.domain.ContainerSubtype;
import liquid.container.service.ContainerSubtypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ContainerSubtypeFormatter.class);

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Override
    public ContainerSubtype parse(String text, Locale locale) throws ParseException {
        logger.debug("Container subtype id is '{}'.", text);
        if (text.length() == 0) {
            logger.warn("Container subtype id '{}' is illegal.", text);
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
