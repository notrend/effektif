/* Copyright (c) 2014, Effektif GmbH.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package com.effektif.workflow.impl.job.types;

import java.util.List;

import com.effektif.workflow.api.ref.UserReference;
import com.effektif.workflow.api.task.Task;
import com.effektif.workflow.api.task.TaskQuery;
import com.effektif.workflow.api.task.TaskService;
import com.effektif.workflow.impl.activity.types.UserTaskImpl;
import com.effektif.workflow.impl.job.AbstractJobType;
import com.effektif.workflow.impl.job.Job;
import com.effektif.workflow.impl.job.JobController;
import com.effektif.workflow.impl.workflow.BindingImpl;
import com.effektif.workflow.impl.workflowinstance.ActivityInstanceImpl;
import com.effektif.workflow.impl.workflowinstance.WorkflowInstanceImpl;


/**
 * @author Tom Baeyens
 */
public class EscalateTaskJobType extends AbstractJobType {

  @Override
  public void execute(JobController jobController) {
    WorkflowInstanceImpl workflowInstance = jobController.getWorkflowInstance();
    Job job = jobController.getJob();
    String taskId = job.getTaskId();
    
    TaskService taskService = jobController.getConfiguration().getTaskService();

    Task task = getTask(taskService, taskId);
    if (task!=null && !task.isCompleted()) {
      String activityInstanceId = job.getActivityInstanceId();
      ActivityInstanceImpl activityInstance = workflowInstance.findActivityInstance(activityInstanceId);
      UserTaskImpl userTaskImpl = (UserTaskImpl) activityInstance.getActivity().getActivityType();
      BindingImpl<UserReference> escalateTo = userTaskImpl.getEscalateTo();
      UserReference escalateToReference = activityInstance.getValue(escalateTo);
      task.assignee(escalateToReference);
      taskService.assignTask(taskId, escalateToReference);
    }
  }

  public Task getTask(TaskService taskService, String taskId) {
    List<Task> tasks = taskService.findTasks(new TaskQuery().taskId(taskId));
    if (tasks!=null && !tasks.isEmpty()) {
      return tasks.get(0);
    }
    return null;
  }
}
