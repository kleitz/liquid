package liquid.poc.controller;

import liquid.mail.model.Mail;
import liquid.mail.service.MailNotificationService;
import liquid.poc.service.PocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: tao
 * Date: 12/2/13
 * Time: 8:12 PM
 */
@Controller
@RequestMapping("/poc")
public class PocController {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PocService pocService;

    @RequestMapping(value = "/{template}", method = RequestMethod.GET)
    public String init(@PathVariable String template) {
        return "common/poc/" + template;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public String sendMail(Mail mailDto) {
        mailNotificationService.send(mailDto.getMailTo(), mailDto.getSubject(), mailDto.getContent());
        return "common/poc/mail";
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String message(String msgKey) {
        String msg = pocService.i18n(msgKey);
        return "redirect:/poc/message?msg=" + msg;
    }
}
