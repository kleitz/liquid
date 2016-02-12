package liquid.poc.restfulapi;

import liquid.poc.model.Address;
import liquid.poc.model.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Tao Ma on 4/17/15.
 */
@Controller
@RequestMapping("/api/poc")
public class PocRestController {

    @RequestMapping("/ajax")
    @ResponseBody
    public Data[] doAjax() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        return new Data[]{
                new Data("UUID-01", UUID.randomUUID().toString()),
                new Data("UUID-02", UUID.randomUUID().toString())
        };
    }

    @RequestMapping("/cities")
    @ResponseBody
    public Iterable<Address> doAlert() {
        sleep(5);
        return Arrays.asList(new Address[]{
            new Address(1, "Harbin"),
            new Address(2, "Beijing"),
            new Address(3, "Kunming")
        });
    }

    private void sleep(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
