package liquid.process.service;

import liquid.process.NotCompletedException;
import liquid.process.domain.Task;
import liquid.process.domain.TaskBar;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao Ma on 1/14/15.
 */
public interface TaskService {
    Task getTask(String taskId);

    Task getTask(String definitionKey, BusinessKey businessKey);

    String pass(String taskId, String reason);

    void assign(String taskId, String username);

    void claim(String taskId, String uid);

    String complete(String taskId) throws NotCompletedException;

    void complete(String taskId, String uid, Map<String, Object> variableMap);

    List<Task> listCompltedTasks(String businessKey);

    Long getOrderIdByTaskId(String taskId);

    Task[] listTasks(String candidateGid);

    Task[] listMyTasks(String uid);

    Task[] listWarningTasks();

    List<Task> findByBusinessKey(String businessKey);

    Object getVariable(String taskId, String variableName);

    void setVariable(String taskId, String variableName, Object value);

    // FIXME - The following two methods are related to web layer and should be moved to somewhere.
    String computeTaskMainPath(String taskId);

    TaskBar calculateTaskBar(String candidateGid, String uid);

    List<String> findCandidateGroups(String taskId);
}
