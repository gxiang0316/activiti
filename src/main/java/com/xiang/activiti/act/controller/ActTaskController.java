package com.xiang.activiti.act.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xiang.activiti.act.util.AjaxJson;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 流程个人任务相关Controller
 * @author guixiang
 * @version 2018-03-25
 */
@Controller
public class ActTaskController{

	@Autowired
	private TaskService taskService;

	/**
	 * 待认领工单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/task/unclaimed")
	public String unClaimedList(Model model)
	{
		String userId = "admin";
		List<Task> unClaimList = taskService.createTaskQuery().taskCandidateUser(userId).active().list();
		model.addAttribute("unClaimedList",unClaimList);
		return "act/task/waitingClaimTask";
	}

	/**
	 * 认领工单
	 * @return
	 */
	@RequestMapping(value="/task/claiming")
	@ResponseBody
	public AjaxJson claiming(@RequestParam("taskId") String taskId)
	{
		AjaxJson j = new AjaxJson();
		String userId = "leaderuser";
		taskService.claim(taskId, userId);
		String message = "认领成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 个人待办工单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/task/claimed")
	public String claimedList(Model model)
	{
		String userId = "leaderuser";
		List<Task> claimedList = taskService.createTaskQuery().taskAssignee(userId).list();
		model.addAttribute("claimedList",claimedList);
		return "act/task/claimedTask";
	}

	/**
	 * 历史工单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/task/finished")
	public String finishedList(Model model)
	{
		String userId = "admin";
		List<Task> claimedList = taskService.createTaskQuery().taskAssignee(userId).list();
		model.addAttribute("claimedList",claimedList);
		return "finishedProcess";
	}





//
//	/**
//	 * 待办任务页面
//	 * @return
//	 */
//	@RequestMapping(params = "claimedTask")
//	public ModelAndView claimedTask()
//	{
//		return new ModelAndView("jeecg/activiti/process/claimedTask");
//	}
//
//	/**
//	 * easyui AJAX请求数据 待办任务
//	 * @param request
//	 * @param response
//	 * @param dataGrid
//	 */
//	@RequestMapping(params = "claimedTaskDataGrid")
//	public void claimedTaskDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
//	{
//		String userId = "admin";
//		JSONObject jObject = actTaskService.claimedTaskDataGrid(userId);
//		responseDatagrid(response, jObject);
//	}
//
//	/**
//	 * 已办任务页面
//	 * @return
//	 */
//	@RequestMapping(params = "finishedTask")
//	public ModelAndView finishedTask()
//	{
//		return new ModelAndView("jeecg/activiti/process/finishedTask");
//	}
//
//	/**
//	 * easyui AJAX请求数据 已办任务
//	 * @param request
//	 * @param response
//	 * @param dataGrid
//	 */
//	@RequestMapping(params = "finishedTaskDataGrid")
//	public void finishedTask(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
//	{
//		String userId = "leaderuser";
//		JSONObject jObject = actTaskService.finishedTask(userId);
//		responseDatagrid(response, jObject);
//	}
//
//	/**
//     * 认领任务
//     * @param taskId
//     */
//	@RequestMapping(params = "claimTask")
//	@ResponseBody
//	public AjaxJson claimTask(@RequestParam("taskId") String taskId, HttpServletRequest request)
//	{
//		AjaxJson j = new AjaxJson();
//		String userId = "leaderuser";
//		actTaskService.claimTask(taskId, userId);
//		String message = "认领成功";
//		j.setMsg(message);
//		return j;
//	}
//
//	/**
//	 * easyui 获取流程历史数据
//	 * @param request
//	 * @param response
//	 * @param dataGrid
//	 */
//	@RequestMapping(params = "taskHistoryList")
//	public void taskHistoryList(@RequestParam("processInstanceId") String processInstanceId,
//			HttpServletRequest request, HttpServletResponse response,DataGrid dataGrid)
//	{
//		JSONObject jObject = actTaskService.taskHistoryList(processInstanceId);
//		responseDatagrid(response, jObject);
//	}
//
//	// -----------------------------------------------------------------------------------
//	// 以下各函数可以提成共用部件 (Add by Quainty)
//	// -----------------------------------------------------------------------------------
//	public void responseDatagrid(HttpServletResponse response, JSONObject jObject)
//	{
//		response.setContentType("application/json");
//		response.setHeader("Cache-Control", "no-store");
//		try
//		{
//			PrintWriter pw=response.getWriter();
//			pw.write(jObject.toString());
//			pw.flush();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//	}
}
