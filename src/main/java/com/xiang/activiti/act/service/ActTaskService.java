package com.xiang.activiti.act.service;

import net.sf.json.JSONObject;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("actTaskService")
@Transactional
public class ActTaskService
{
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 获取待领任务列表
	 * @param userId
	 */
	public JSONObject unClaimTaskDataGrid(String userId) 
	{
		TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(userId).active().list();
		StringBuffer rows = new StringBuffer();
		for(Task t : tasks)
		{
			rows.append("{'name':'"+t.getName() +"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		return  JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
	}

	/**
	 * 获取个人待办任务
	 * @param userId
	 */
	public JSONObject claimedTaskDataGrid(String userId)
	{
		TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		
		StringBuffer rows = new StringBuffer();
		for(Task t : tasks)
		{
			rows.append("{'name':'"+t.getName() +"','description':'"+t.getDescription()+"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		return JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
	}
	
	/**
	 * 获取个人已办任务
	 * @param userId
	 */
	public JSONObject finishedTask(String userId) 
	{
		List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery().taskAssignee(userId)
                .finished().list();
		StringBuffer rows = new StringBuffer();
		for(HistoricTaskInstance t : historicTasks)
		{
			rows.append("{'name':'"+t.getName() +"','description':'"+t.getDescription()+"','taskId':'"+t.getId()+"','processDefinitionId':'"+t.getProcessDefinitionId()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		return JSONObject.fromObject("{'total':"+historicTasks.size()+",'rows':["+rowStr+"]}");
	}
	
	/**
	 * 认领任务
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public void claimTask(String taskId, String userId)
	{
		TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);
	}
	
	/**
	 * 获取历史数据
	 * @param processInstanceId
	 */
	public JSONObject taskHistoryList(String processInstanceId)
	{
		List<HistoricTaskInstance> historicTasks = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).list();
		StringBuffer rows = new StringBuffer();
		for (HistoricTaskInstance hi : historicTasks) {
			rows.append("{'name':'" + hi.getName() + "','processInstanceId':'"
					+ hi.getProcessInstanceId() + "','startTime':'"
					+ hi.getStartTime() + "','endTime':'" + hi.getEndTime()
					+ "','assignee':'" + hi.getAssignee()
					+ "','deleteReason':'" + hi.getDeleteReason() + "'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		return JSONObject.fromObject("{'total':" + historicTasks.size()
				+ ",'rows':[" + rowStr + "]}");
	}
}
