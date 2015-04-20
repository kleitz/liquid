package liquid.poc.restfulapi;

import liquid.poc.model.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        } catch (InterruptedException e) { }

        return new Data[]{
                new Data("UUID-01", UUID.randomUUID().toString()),
                new Data("UUID-02", UUID.randomUUID().toString())
        };
    }
}
