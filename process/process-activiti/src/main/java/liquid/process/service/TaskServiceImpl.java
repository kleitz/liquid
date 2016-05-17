package liquid.process.service;

import liquid.process.NotCompletedException;
import liquid.process.domain.Task;
import liquid.process.domain.TaskBar;
import liquid.process.handler.TaskHandler;
import liquid.process.handler.TaskHandlerFactory;
import liquid.user.domain.UserProfile;
import liquid.user.service.UserService;
import liquid.util.DatePattern;
import liquid.util.DateUtil;
import liquid.util.StringUtil;
import org.activiti.bpmn.model.Activity;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: tao
 * Date: 10/18/13
 * Time: 11:12 PM
 */
@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    protected TaskHandlerFactory taskFactory;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    private UserService userService;

//    @Autowired
//    private UserService accountService;

//    @Autowired
//    private MailNotificationService mailNotificationService;

    @Override
    public Task getTask(String taskId) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        Task task = toTask(activitiTask);

        String prompt = messageSource.getMessage("task.complete.prompt." + activitiTask.getTaskDefinitionKey(), new Object[0], "", Locale.CHINA);
        if (!StringUtil.valid(prompt))
            prompt = messageSource.getMessage("task.complete.prompt.default", new Object[0], "", Locale.CHINA);
        task.setPrompt(prompt);
        return task;
    }

    @Override
    public Task getTask(String definitionKey, BusinessKey businessKey) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskDefinitionKey(definitionKey).processInstanceBusinessKey(businessKey.getText()).singleResult();
        if (null == activitiTask) return null;
        return toTask(activitiTask);
    }

    @Override
    public String pass(String taskId, String reason) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == activitiTask) {
            logger.warn("The taskId '{}' is null.", taskId);
            return null;
        }
        taskService.addComment(taskId, activitiTask.getProcessInstanceId(), reason);
        taskService.complete(taskId);
        return getBusinessKeyByProcessInstanceId(activitiTask.getProcessInstanceId()).getText();
    }

    @Override
    public void assign(String taskId, String username) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        taskService.setAssignee(taskId, username);
    }

    @Override
    public void claim(String taskId, String uid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, uid);

//        User user = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("claim.task", null, Locale.CHINA),
//                messageSource.getMessage("claim.task.content", new String[]{uid}, Locale.CHINA),
//                user.getEmail());
    }

    @Override
    public String complete(String taskId) throws NotCompletedException {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == activitiTask) {
            logger.warn("The taskId '{}' is null.", taskId);
            return null;
        }
        TaskHandler task = taskFactory.locateHandler(activitiTask.getTaskDefinitionKey());
        task.complete(taskId);
        return getBusinessKeyByProcessInstanceId(activitiTask.getProcessInstanceId()).getText();
    }

    @Override
    public void complete(String taskId, String uid, Map<String, Object> variableMap) {
        variableMap.put("employeeName", uid);
        variableMap.put("endTime", DateUtil.stringOf(Calendar.getInstance().getTime(), DatePattern.UNTIL_SECOND));
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId, variableMap);

//        User user = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("complete.task", null, Locale.CHINA),
//                messageSource.getMessage("complete.task.content", new String[]{uid}, Locale.CHINA),
//                user.getEmail());
    }

    @Override
    public List<Task> listCompltedTasks(String businessKeyText) {
        List<Task> tasks = new ArrayList<>();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKeyText).list();
        for (HistoricTaskInstance historicTask : historicTasks) {
            Task task = new Task();
            task.setId(historicTask.getId());
            task.setDefinitionKey(historicTask.getTaskDefinitionKey());
            task.setName(historicTask.getName());
            task.setDescription(historicTask.getDescription());
            task.setAssignee(historicTask.getAssignee());
            task.setCreateDate(DateUtil.stringOf(historicTask.getCreateTime(), DatePattern.UNTIL_SECOND));
            task.setStartTime(historicTask.getStartTime());
            task.setClaimTime(historicTask.getClaimTime());
            task.setEndTime(historicTask.getEndTime());
            BusinessKey businessKey = getBusinessKeyByProcessInstanceId(historicTask.getProcessInstanceId());
            task.setOrderId(businessKey.getOrderId());
            task.setOrderNo(businessKey.getOrderNo());
            if(null == historicTask.getEndTime()) {
                List<UserProfile> userList = new ArrayList<>();
                List<String> candidateGroupList = findCandidateGroups(task.getId());
                for (String group : candidateGroupList) {
                    userList.addAll(userService.findByGroup(group));
                }
                if(0 == candidateGroupList.size() && null != task.getAssignee()) {
                    userList.add(userService.findByUid(task.getAssignee()));
                }
                task.setCandidateUserList(userList);
            }
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public boolean isFinished(BusinessKey businessKey, String activityId) {
        List<HistoricActivityInstance> historicActivityInstanceList = listHistoricActivities(businessKey, activityId);
        for (HistoricActivityInstance historicActivityInstance: historicActivityInstanceList) {
            if(historicActivityInstance.getEndTime() != null) {
                continue;
            } else{
                return false;
            }
        }
        return true;
    }

    private List<HistoricActivityInstance> listHistoricActivities(BusinessKey businessKey, String activityId) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey.getText()).singleResult();

        List<HistoricActivityInstance> historicActivityInstanceList = historyService.
                createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).activityId(activityId).list();

        return historicActivityInstanceList;
    }

    @Override
    public Long getOrderIdByTaskId(String taskId) {
        BusinessKey businessKey = getBusinessKeyByTaskId(taskId);
        return businessKey.getOrderId();
    }

    @Override
    public Task[] listTasks(String candidateGid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<org.activiti.engine.task.Task> activitiTasks =
                taskService.createTaskQuery().orderByTaskCreateTime().asc().taskCandidateGroup(candidateGid).list();
        return toTasks(activitiTasks);
    }

    @Override
    public Task[] listMyTasks(String uid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<org.activiti.engine.task.Task> activitiTasks =
                taskService.createTaskQuery().orderByTaskCreateTime().asc().taskAssignee(uid).list();
        return toTasks(activitiTasks);
    }

    @Override
    public Task[] listWarningTasks() {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<org.activiti.engine.task.Task> activitiTasks =
                taskService.createTaskQuery().taskDefinitionKey("recordTod").list();
        Iterator<org.activiti.engine.task.Task> iterator = activitiTasks.iterator();
        while (iterator.hasNext()) {
            org.activiti.engine.task.Task task = iterator.next();
            Date now = new Date();
            Date created = task.getCreateTime();
            if ((now.getTime() - created.getTime()) < 2 * 24 * 60 * 60 * 1000) iterator.remove();
        }
        return toTasks(activitiTasks);
    }

    @Override
    public List<Task> findByBusinessKey(String businessKey) {
        List<Task> tasks = new ArrayList<>();
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<org.activiti.engine.task.Task> activitiTasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
        for (org.activiti.engine.task.Task activitiTask: activitiTasks) {
            tasks.add(toTask(activitiTask));
        }
        return tasks;
    }

    public Object getVariable(String taskId, String variableName) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processEngine.getRuntimeService().getVariable(activitiTask.getProcessInstanceId(), variableName);
    }

    public void setVariable(String taskId, String variableName, Object value) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        processEngine.getRuntimeService().setVariable(activitiTask.getProcessInstanceId(), variableName, value);
    }

    @Override
    public String computeTaskMainPath(String taskId) {
        Task task = getTask(taskId);
        return computeTaskMainPath(task);
    }

    private String computeTaskMainPath(Task task) {
        switch (task.getDefinitionKey()) {
            case "feedDistyPrice":
                return "/dp?t=" + task.getId();
            case "planRoute": // FIXME - Will delete it after GA.
                return "/task/" + task.getId() + "/planning";
            case "allocateContainers":
            case "feedContainerNo":
                return "/task/" + task.getId() + "/allocation";
            case "deliver":
                return "/task/" + task.getId() + "/delivery";
            case "checkCostByMarketing":
            case "checkCostByOperating":
            case "checkFromMarketing":
            case "checkFromOperating":
                return "/task/" + task.getId() + "/check_amount";
            default:
                return "/task/" + task.getId() + "/common";
        }
    }

    @Override
    public TaskBar calculateTaskBar(String candidateGid, String uid) {
        TaskBar taskBadge = new TaskBar();
        Task[] queue = listTasks(candidateGid);
        Task[] myTasks = listMyTasks(uid);
        Task[] warnings = listWarningTasks();

        taskBadge.setTaskQueueSize(queue.length);
        taskBadge.setMyTasksSize(myTasks.length);
        taskBadge.setWarningsSize(warnings.length);
        return taskBadge;
    }

    @Override
    public List<String> findCandidateGroups(String taskId) {
        List<String> candidateGroupList = new ArrayList<>();
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        try {
            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
            for (IdentityLink identifyLink: identityLinkList) {
                if(IdentityLinkType.CANDIDATE.equals(identifyLink.getType())) {
                    candidateGroupList.add(identifyLink.getGroupId());
                } else {
                    logger.warn("The identity link type '{}' is illegal.", identifyLink.getGroupId());
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return candidateGroupList;
    }

    private Task[] toTasks(List<org.activiti.engine.task.Task> activitiTasks) {
        Task[] tasks = new Task[activitiTasks.size()];
        for (int i = 0; i < tasks.length; i++) {
            org.activiti.engine.task.Task activitiTask = activitiTasks.get(i);
            tasks[i] = toTask(activitiTask);
        }
        return tasks;
    }

    private Task toTask(org.activiti.engine.task.Task activitiTask) {
        Task task = new Task();
        task.setId(activitiTask.getId());
        task.setDefinitionKey(activitiTask.getTaskDefinitionKey());
        task.setName(activitiTask.getName());
        task.setDescription(activitiTask.getDescription());
        task.setAssignee(activitiTask.getAssignee());
        task.setCreateDate(DateUtil.stringOf(activitiTask.getCreateTime(), DatePattern.UNTIL_SECOND));
        BusinessKey businessKey = getBusinessKeyByProcessInstanceId(activitiTask.getProcessInstanceId());
        task.setOrderId(businessKey.getOrderId());
        task.setOrderNo(businessKey.getOrderNo());
        return task;
    }

    private BusinessKey getBusinessKeyByProcessInstanceId(String processInstanceId) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // If process has been completed, the processInstance could be null.
        if(null == processInstance) {
            HistoryService historyService = processEngine.getHistoryService();
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String text = historicProcessInstance.getBusinessKey();
            return BusinessKey.decode(text);
        } else {
            String text = processInstance.getBusinessKey();
            return BusinessKey.decode(text);
        }
    }

    private BusinessKey getBusinessKeyByTaskId(String taskId) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        return getBusinessKeyByProcessInstanceId(activitiTask.getProcessInstanceId());
    }
}
