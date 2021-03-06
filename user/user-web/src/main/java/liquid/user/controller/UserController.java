package liquid.user.controller;

import liquid.core.controller.BaseController;
import liquid.core.model.Alert;
import liquid.user.domain.GroupMember;
import liquid.user.model.PasswordChange;
import liquid.user.model.User;
import liquid.user.service.UserService;
import liquid.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collection;

/**
 * User: tao
 * Date: 10/6/13
 * Time: 11:50 AM
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Collection<User> list = userService.findAll();
        model.addAttribute("users", list);
        return "user/list";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String initRegister(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("groups", userService.findGroups());
        return "user/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult bindingResult,
                           Model model, RedirectAttributes redirectAttributes) {
        logger.debug("user: {}", user);
        if (!bindingResult.hasErrors()) {
            if (user.getPassword().equals(user.getPassword2())) {
                if(null != userService.find(user.getUid())) {
                    bindingResult.rejectValue("uid", "user.already.exists");
                } else {
                    userService.register(user);
                    redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
                    return "redirect:/user/register";
                }
            } else {
                addFieldError(bindingResult, "user", "password2", user.getPassword2(), user.getPassword2());
            }
        }
        model.addAttribute("groups", userService.findGroups());
        return "user/register";
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public String initSettings(@PathVariable String uid, Model model) {
        logger.debug("uid: {}", uid);

        prepareApplyView(model, uid);

        PasswordChange passwordChange = new PasswordChange();
        passwordChange.setUid(uid);
        model.addAttribute("passwordChange", passwordChange);
        return "user/edit";
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=unlock")
    public String unlock(@PathVariable String uid, RedirectAttributes redirectAttributes) {
        logger.debug("uid: {}", uid);

        userService.unlock(uid);
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/user/" + StringUtil.utf8encode(uid);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=lock")
    public String lock(@PathVariable String uid, RedirectAttributes redirectAttributes) {
        logger.debug("uid: {}", uid);

        userService.lock(uid);
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        // TODO: The following way is workaround. There must be a best way to solve encoding issue.
        return "redirect:/user/" + StringUtil.utf8encode(uid);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=changeProfile")
    public String apply(@PathVariable String uid, User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.debug("uid: {}", uid);
        logger.debug("user: {}", user);
        if (bindingResult.hasErrors()) {
            return "user/edit";
        } else {
            userService.edit(user);
            redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        }

        return "redirect:/user/" + StringUtil.utf8encode(uid);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=assignToGroup")
    public String assignToGroup(@PathVariable String uid, String group, RedirectAttributes redirectAttributes) {
        logger.debug("uid: {}", uid);
        logger.debug("group: {}", group);
        userService.assignToGroup(uid, Integer.valueOf(group));
        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/user/" + StringUtil.utf8encode(uid);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=resetPassword")
    public String resetPassword(@PathVariable String uid,
                                PasswordChange passwordChange, BindingResult bindingResult,
                                Model model, RedirectAttributes redirectAttributes) {
        logger.debug("uid: {}", uid);
        if (!bindingResult.hasErrors()) {
            if (passwordChange.getNewPassword().equals(passwordChange.getNewPassword2())) {
                userService.changePassword(uid, passwordChange.getNewPassword());
                model.addAttribute("alert", new Alert("save.success"));
                redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
                return "redirect:/user/" + StringUtil.utf8encode(uid);
            } else {
                addFieldError(bindingResult, "passwordChange", "newPassword2", passwordChange.getOldPassword(), passwordChange.getOldPassword());
            }
        }
        prepareApplyView(model, uid);
        return "user/edit";
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.POST, params = "action=changePassword")
    public String changePassword(@PathVariable String uid,
                                 // BindingResult must be next to the validating object
                                 @Valid PasswordChange passwordChange, BindingResult bindingResult,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            if (passwordChange.getNewPassword().equals(passwordChange.getNewPassword2())) {
                try {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(uid, passwordChange.getOldPassword());
                    authenticationManager.authenticate(authentication);
                    userService.changePassword(uid, passwordChange.getNewPassword());
                    redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
                    return "redirect:/user/" + StringUtil.utf8encode(uid);
                } catch (AuthenticationException e) {
                    addFieldError(bindingResult, "passwordChange", "oldPassword", passwordChange.getOldPassword(), passwordChange.getOldPassword());
                }
            } else {
                addFieldError(bindingResult, "passwordChange", "newPassword2", passwordChange.getOldPassword(), passwordChange.getOldPassword());
            }
        }
        prepareApplyView(model, uid);
        return "user/edit";
    }

    private void prepareApplyView(Model model, String uid) {
        User user = userService.find(uid);
        GroupMember member = userService.findByUsername(uid);
        user.setGroup(String.valueOf(member.getGroup().getId()));
        model.addAttribute("user", user);
        model.addAttribute("groups", userService.findGroups());
    }
}
