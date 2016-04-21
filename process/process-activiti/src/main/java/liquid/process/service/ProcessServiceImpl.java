package liquid.process.service;

import liquid.util.DatePattern;
import liquid.util.DateUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Ma on 4/4/15.
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

//    private String processDefinitionKey = "shipping";
//    private String definitionClasspath = "processes/liquid.shipping.bpmn20.xml";

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public void startProcess(String serviceTypeCode, String uid, BusinessKey businessKey, Map<String, Object> variableMap) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Process process = getProcess(serviceTypeCode);
        deployProcess(process);

        variableMap.put("employeeName", uid);
        variableMap.put("endTime", DateUtil.stringOf(Calendar.getInstance().getTime(), DatePattern.UNTIL_SECOND));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process.getKey(), businessKey.getText(), variableMap);
        runtimeService.addUserIdentityLink(processInstance.getId(), uid, IdentityLinkType.STARTER);

        // FIXME email notification.
//        Account account = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("process.start", null, Locale.CHINA),
//                messageSource.getMessage("process.start.content", new String[]{uid}, Locale.CHINA),
//                account.getEmail());
    }

    private void deployProcess(Process process) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(process.getKey()).latestVersion().singleResult();
        // If there are something changed in definition file located definitionClasspath, activiti will auto-deploy it.
        // if (null == processDefinition)
            repositoryService.createDeployment().addClasspathResource(process.getClasspath()).deploy();
    }

    private Process getProcess(String serviceTypeCode) {
        switch (serviceTypeCode) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                return new Process("shipping", "processes/liquid.shipping.bpmn20.xml");
            case "5":
            case "6":
                return new Process("process_pool1", "processes/liquid.truck.bpmn20.xml");
            default:
                throw new IllegalArgumentException(serviceTypeCode);
        }
    }

    @Override
    public void listProcessDefinitions(String key) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey(key).orderByProcessDefinitionVersion().asc().list();
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println(processDefinition);
        }
    }

    private static class Process {
        private final String key;
        private final String classpath;

        public Process(String key, String classpath) {
            this.key = key;
            this.classpath = classpath;
        }

        public String getKey() {
            return key;
        }

        public String getClasspath() {
            return classpath;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Process{");
            sb.append("super=").append(super.toString()).append('\'');
            sb.append(", key='").append(key).append('\'');
            sb.append(", classpath='").append(classpath).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public void messageEventReceived(BusinessKey businessKey){
        RuntimeService runtimeService= processEngine.getRuntimeService();
        Execution processInstance = runtimeService.createExecutionQuery().processInstanceBusinessKey(businessKey.getText()).singleResult();
        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("changeOrder")
                .processInstanceId(processInstance.getId())
                .singleResult();
        runtimeService.messageEventReceived("changeOrder", execution.getId());
    }
}
