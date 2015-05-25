package liquid.container.controller;

import liquid.container.domain.*;
import liquid.container.facade.ContainerFacade;
import liquid.container.model.Container;
import liquid.container.model.Containers;
import liquid.container.service.InternalContainerService;
import liquid.container.service.InternalContainerSubtypeService;
import liquid.core.controller.BaseController;
import liquid.core.model.Pagination;
import liquid.core.model.SearchBarForm;
import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.LocationService;
import liquid.order.service.ServiceItemService;
import liquid.transport.domain.RailContainer;
import liquid.transport.service.RailContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: tao
 * Date: 10/3/13
 * Time: 3:59 PM
 */
@Controller
@RequestMapping("/container")
public class ContainerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    private Environment env;

    @Autowired
    private InternalContainerService containerService;

    @Autowired
    private ContainerFacade containerFacade;

    @Autowired
    private LocationService locationService;

    @Autowired
    private InternalContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private RailContainerService railContainerService;

    @ModelAttribute("container")
    public Container populateContainer() {
        return new Container();
    }

    @ModelAttribute("statusArray")
    public ContainerStatus[] populateStatus() {
        return ContainerStatus.values();
    }

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypeMap() {
        return ContainerCap.toMap();
    }

    @ModelAttribute("containerTypes")
    public ContainerCap[] populateContainerTypes() {
        return ContainerCap.values();
    }

    @ModelAttribute("locations")
    public List<Location> populateLocations() {
        return locationService.findByTypeId(LocationType.YARD);
    }

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtype> populateContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("owners")
    public Iterable<ServiceProvider> populateOwners() {
        return serviceItemService.findContainerOwners();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Pagination pagination,
                       @RequestParam(required = false, defaultValue = "0") Long subtypeId,
                       @RequestParam(required = false, defaultValue = "0") Long ownerId,
                       @RequestParam(required = false, defaultValue = "0") Long yardId,
                       @RequestParam(required = false) String bicCode,
                       Model model, HttpServletRequest request) {

        // container subtypes
        Iterable<ContainerSubtype> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);

        // Owner list
        List<ServiceProvider> owners = serviceItemService.findContainerOwners();

        // Yard list
        List<Location> yards = locationService.findYards();

        if (null != bicCode && bicCode.trim().length() > 0) {
            List<ContainerEntity> containers = containerService.findBicCode(bicCode);
            if (null == containers) containers = new ArrayList<>();
            final List<ContainerEntity> list = containers;
            model.addAttribute("page", new PageImpl<ContainerEntity>(list));
        } else {
            PageRequest pageRequest = new PageRequest(pagination.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
            Page<ContainerEntity> page = containerService.findAll(subtypeId, ownerId, yardId, pageRequest);
            model.addAttribute("page", page);
            model.addAttribute("contextPath", "/container?subtypeId=" + subtypeId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");
        }

        model.addAttribute("subtypeId", subtypeId);
        model.addAttribute("subtypes", subtypes);

        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owners", owners);

        model.addAttribute("yardId", yardId);
        model.addAttribute("yards", yards);

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/container");
        model.addAttribute("searchBarForm", searchBarForm);
        return "container/list";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"search"})
    public String search(SearchBarForm searchBarForm, Model model,
                         @RequestParam(required = false, defaultValue = "0") int number,
                         @RequestParam(required = false, defaultValue = "0") Long subtypeId,
                         @RequestParam(required = false, defaultValue = "0") Long ownerId,
                         @RequestParam(required = false, defaultValue = "0") Long yardId) {
        PageRequest request = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findByBicCodeLike(searchBarForm.getText(), request);

        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/container?subtypeId=" + subtypeId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");

        // container subtypes
        Iterable<ContainerSubtype> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);

        // Owner list
        List<ServiceProvider> owners = serviceItemService.findContainerOwners();

        // Yard list
        List<Location> yards = locationService.findYards();

        model.addAttribute("subtypeId", subtypeId);
        model.addAttribute("subtypes", subtypes);

        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owners", owners);

        model.addAttribute("yardId", yardId);
        model.addAttribute("yards", yards);

        searchBarForm.setAction("/container");
        model.addAttribute("searchBarForm", searchBarForm);
        return "container/list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {

        Containers containers = new Containers();
        for (int i = 0; i < 10; i++) {
            containers.getList().add(new Container());
        }

        model.addAttribute("containers", containers);
        return "container/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initForm(@PathVariable Long id, Model model) {
        Container container = containerFacade.find(id);
        Containers containers = new Containers();
        containers.getList().add(container);
        model.addAttribute("containers", containers);
        return "container/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute Containers containers, BindingResult bindingResult) {
        logger.debug("container: {}", containers);

        if (bindingResult.hasErrors()) {
            return "container/list";
        } else {
            containerFacade.enter(containers);
            return "redirect:/container";
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String initImport(Model model) {
        ExcelFileInfo[] excelFileInfos = new ExcelFileInfo[0];
        try {
            excelFileInfos = containerService.readMetadata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("fileInfos", excelFileInfos);
        return "container/import";
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET, params = "filename")
    public String importFile(@RequestParam final String filename, Model model) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    containerService.importExcel(filename);
                    logger.info("{} import is done.", filename);
                } catch (IOException e) {
                    logger.info("{} import failed.", filename);
                }
            }
        }).start();

        return "redirect:/container/import";
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                containerService.writeToFile(file.getOriginalFilename(), file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/container/import";
    }

    @RequestMapping(value = "/release/now", method = RequestMethod.GET)
    public String listReleasedNow(Model model) {
        Iterable<RailContainer> containers = railContainerService.findByReleasedAtToday();

        model.addAttribute("containers", containers);
        model.addAttribute("tab", "now");
        return "container/now_release";
    }

    @RequestMapping(value = "/release/all", method = RequestMethod.GET)
    public String listReleasedAll(Pagination pagination, Model model, HttpServletRequest request) {
        PageRequest pageRequest = new PageRequest(pagination.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<RailContainer> page = railContainerService.findAll(pageRequest);
        
        pagination.prepand(request.getRequestURI());
        model.addAttribute("page", page);
        model.addAttribute("tab", "all");
        return "container/all_release";
    }
}
