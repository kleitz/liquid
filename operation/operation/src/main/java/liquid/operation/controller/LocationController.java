package liquid.operation.controller;

import liquid.core.controller.BaseController;
import liquid.core.model.Alert;
import liquid.core.model.AlertType;
import liquid.core.model.Pagination;
import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.model.LocationForm;
import liquid.operation.service.InternalLocationService;
import liquid.operation.service.LocationTypeService;
import liquid.pinyin4j.PinyinHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tao
 * Date: 10/5/13
 * Time: 11:09 AM
 */
@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {
    private final static String ROOT_DIR = "operation/location/";
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private InternalLocationService locationService;

    @Autowired
    private LocationTypeService locationTypeService;

    @ModelAttribute("locationTypes")
    public Iterable<LocationType> populateLocationTypes() {
        return locationTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(SearchBarForm searchBarForm,
                       Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));

        Byte typeId = null;
        if(searchBarForm.getType() != null){
            typeId = Byte.valueOf(searchBarForm.getType());
        }
        Page<Location> page = locationService.findAll(searchBarForm.getText(), typeId, pageRequest);
        searchBarForm.prepand(request.getRequestURI());
        model.addAttribute("page", page);

        return ROOT_DIR + "list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("location", new LocationForm());
        return ROOT_DIR + "form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute LocationForm location,
                      BindingResult bindingResult, Model model) {
        logger.debug("location: {}", location);

        if (bindingResult.hasErrors()) {
            return ROOT_DIR + "form";
        } else {
            try {
                List<Location> entities = new ArrayList<>();
                for (Byte type : location.getTypes()) {
                    Location entity = new Location();
                    entity.setName(location.getName());
                    entity.setType(new LocationType(type));
                    String queryName = PinyinHelper.converterToFirstSpell(location.getName()) + ";" + location.getName();
                    entity.setQueryName(queryName);
                    entities.add(entity);
                }
                locationService.save(entities);
                return "redirect:/location";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", new Alert("duplicated.key"));
                return ROOT_DIR + "form";
            }
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("location", locationService.find(id));
        return ROOT_DIR + "edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute("location") Location location,
                       BindingResult bindingResult, Model model) {
        logger.debug("location: {}", location);

        if (bindingResult.hasErrors()) {
            return ROOT_DIR + "edit";
        } else {
            try {
                locationService.save(location);
                return "redirect:/location";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", new Alert(AlertType.DANGER, "duplicated.key"));
                return ROOT_DIR + "edit";
            }
        }
    }
}
