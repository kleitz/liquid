package liquid.operation.controller;

import liquid.core.controller.BaseController;
import liquid.core.model.Alert;
import liquid.core.model.AlertType;
import liquid.core.model.Pagination;
import liquid.operation.domain.Goods;
import liquid.operation.service.InternalGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * User: tao
 * Date: 9/28/13
 * Time: 3:30 PM
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {
    private final static String ROOT_DIR = "operation/goods/";

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private InternalGoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pagination pagination,
                       Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(pagination.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<Goods> page = goodsService.findAll(pageRequest);

        model.addAttribute("goods", new Goods());

        pagination.prepand(request.getRequestURI());
        model.addAttribute("page", page);
        return ROOT_DIR + "list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("goods", new Goods());
        return ROOT_DIR + "form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("goods", goodsService.find(id));
        return ROOT_DIR + "form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("goods") Goods goods,
                         BindingResult bindingResult, Model model) {
        logger.debug("Goods: {}", goods);

        if (bindingResult.hasErrors()) {
            return ROOT_DIR + "form";
        } else {
            try {
                goodsService.save(goods);
                return "redirect:/goods";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", new Alert(AlertType.DANGER, "duplicated.key"));
                return ROOT_DIR + "form";
            }
        }
    }
}
