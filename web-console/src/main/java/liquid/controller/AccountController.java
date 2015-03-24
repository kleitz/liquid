package liquid.controller;

import liquid.user.model.PasswordChange;
import liquid.user.model.User;
import liquid.user.service.UserService;
import liquid.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 11:50 AM
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    @Qualifier("db")
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Collection<User> list = userService.findAll();
        model.addAttribute("accounts", list);
        return "account/list";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String initRegister(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("groups", userService.findGroups());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult bindingResult) {
        logger.debug("user: {}", user);
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            if (user.getPassword().equals(user.getPassword2())) {
                userService.register(user);
                return "account/success";
            } else {
                ObjectError objectError = new ObjectError("password", "passwords are not same.");
                bindingResult.addError(objectError);
                return "register";
            }
        }
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET, params = "action")
    public String lock(@PathVariable String uid, @RequestParam String action) {
        logger.debug("uid: {}", uid);
        logger.debug("action: {}", action);

        if ("unlock".equals(action)) {
            userService.unlock(uid);
        } else if ("lock".equals(action)) {
            userService.lock(uid);
        } else {
            logger.warn("No matched action handler.");
        }

        return "redirect:/account";
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public String initEdit(@PathVariable String uid, Model model) {
        logger.debug("uid: {}", uid);

        User user = userService.find(uid);
        model.addAttribute("user", user);
        model.addAttribute("groups", userService.findGroups());
        return "account/edit";
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST)
    public String edit(@PathVariable String uid, User user, BindingResult bindingResult) {
        logger.debug("uid: {}", uid);
        logger.debug("user: {}", user);
        if (bindingResult.hasErrors()) {
            return "account/edit";
        } else {
            userService.edit(user);
        }

        return "redirect:/account";
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String initPassword0(Model model, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        LdapUserDetails userDetails = (LdapUserDetails) token.getPrincipal();
        String uid = userDetails.getUsername();

        PasswordChange passwordChange = new PasswordChange();
        passwordChange.setUid(uid);
        model.addAttribute("passwordChange", passwordChange);

        return "account/password_change";
    }

    @RequestMapping(value = "/password/{uid}", method = RequestMethod.GET)
    public String initPassword(@PathVariable String uid, Model model) {
        logger.debug("uid: {}", uid);

        PasswordChange passwordChange = new PasswordChange();
        passwordChange.setUid(uid);
        model.addAttribute("passwordChange", passwordChange);

        return "account/password_change";
    }

    @RequestMapping(value = "/password/{uid}", method = RequestMethod.POST)
    public String resetPassword(@PathVariable String uid, PasswordChange passwordChange,
                                BindingResult bindingResult, Model model) {
        logger.debug("uid: {}", uid);
        logger.debug("account: {}", passwordChange);
        if (bindingResult.hasErrors()) {
            return "account/password_change";
        } else {
            if (passwordChange.getNewPassword().equals(passwordChange.getNewPassword2())) {
                userService.resetPassword(uid, passwordChange);
                model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
                return "account/password_change";
            } else {
                ObjectError objectError = new ObjectError("newPassword", "passwords are not same.");
                bindingResult.addError(objectError);
                return "account/password_change";
            }
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String updatePassword(PasswordChange passwordChange,
                                 BindingResult bindingResult, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        LdapUserDetails userDetails = (LdapUserDetails) token.getPrincipal();
        String uid = userDetails.getUsername();
        String userDn = userDetails.getDn();

        logger.debug("account: {}", passwordChange);
        if (bindingResult.hasErrors()) {
            return "account/password_change";
        } else {
            if (passwordChange.getNewPassword().equals(passwordChange.getNewPassword2())) {
                if (userService.authenticate(userDn, passwordChange.getOldPassword())) {
                    userService.resetPassword(uid, passwordChange);
                    return "account/password_change";
                } else {
                    ObjectError objectError = new ObjectError("oldPassword", "old passwords are not correct.");
                    bindingResult.addError(objectError);
                    return "account/password_change";
                }
            } else {
                ObjectError objectError = new ObjectError("newPassword", "passwords are not same.");
                bindingResult.addError(objectError);
                return "account/password_change";
            }
        }
    }
}
