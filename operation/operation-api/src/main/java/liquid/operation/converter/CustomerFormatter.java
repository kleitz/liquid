package liquid.operation.converter;

import liquid.operation.domain.Customer;
import liquid.operation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 5/18/15.
 */
@Component
public class CustomerFormatter implements Formatter<Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public Customer parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return customerService.findByName(text);
    }

    @Override
    public String print(Customer object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getName());
    }
}
