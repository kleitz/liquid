package liquid.core.converter;

import liquid.core.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 4/2/15.
 * There is a discussion about how to avoid a lot of database lookup.
 * <a href="https://jira.spring.io/browse/SPR-12472">https://jira.spring.io/browse/SPR-12472</a>
 */
public abstract class GenericFormatter<E extends Text> implements Formatter<E> {
    private static final Logger logger = LoggerFactory.getLogger(GenericFormatter.class);

    @Autowired
    private CrudService<E> service;

    @Override
    public E parse(String text, Locale locale) throws ParseException {
        logger.debug("Converting String ({}) to Object.", text);
        if (text.length() == 0) {
            return null;
        }
        return service.find(Long.valueOf(text));
    }

    @Override
    public String print(E object, Locale locale) {
        logger.debug("Converting object ({}) to String.", object);
        if (null == object) {
            return "";
        }
        return object.toText();
    }
}
