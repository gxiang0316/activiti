package com.xiang.activiti.act.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiang.activiti.act.service.ActTaskService;
import com.xiang.activiti.act.util.AjaxJson;
import net.sf.json.JSONObject;

import org.activiti.bpmn.model.DataGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 * 流程个人任务相关Controller
 * @author guixiang
 * @version 2018-03-25
 */
@RestController
@RequestMapping("/actTaskController")
public class ActTaskController{

	@Autowired
	private ActTaskService actTaskService;
	
	/**
	 * 获取流程列表
	 */
	@RequestMapping(params = "process")
	public ModelAndView process(HttpServletRequest request)
	{
		return new ModelAndView("jeecg/activiti/my/myProcessList");
	}
	
	/**
	 * 流程启动表单选择
	 */
	@RequestMapping(params = "startPageSelect")
	public ModelAndView startPageSelect(@RequestParam("startPage") String startPage,HttpServletRequest request)
	{
		return new ModelAndView("jeecg/activiti/my/"+startPage.substring(0, startPage.lastIndexOf(".")));
	}

	/**
	 * 待领任务页面
	 * @return
	 */
	@RequestMapping(params = "unClaimTask")
	public ModelAndView unClaimTask() 
	{
		return new ModelAndView("jeecg/activiti/process/waitingClaimTask");
	}
	
	/**
	 * easyui AJAX请求数据 待领任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "unClaimTaskDataGrid")
	public void unClaimTaskDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
	{
		String userId = "admin";
		JSONObject jObject = actTaskService.unClaimTaskDataGrid(userId);
		responseDatagrid(response, jObject);
	}

	/**
	 * 待办任务页面
	 * @return
	 */
	@RequestMapping(params = "claimedTask")
	public ModelAndView claimedTask()
	{
		return new ModelAndView("jeecg/activiti/process/claimedTask");
	}
	
	/**
	 * easyui AJAX请求数据 待办任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "claimedTaskDataGrid")
	public void claimedTaskDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
	{
		String userId = "admin";
		JSONObject jObject = actTaskService.claimedTaskDataGrid(userId);
		responseDatagrid(response, jObject);
	}

	/**
	 * 已办任务页面
	 * @return
	 */
	@RequestMapping(params = "finishedTask")
	public ModelAndView finishedTask() 
	{
		return new ModelAndView("jeecg/activiti/process/finishedTask");
	}
	
	/**
	 * easyui AJAX请求数据 已办任务
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "finishedTaskDataGrid")
	public void finishedTask(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) 
	{
		String userId = "leaderuser";
		JSONObject jObject = actTaskService.finishedTask(userId);
		responseDatagrid(response, jObject);
	}

	/**
     * 认领任务
     * @param taskId
     */
	@RequestMapping(params = "claimTask")
	@ResponseBody
	public AjaxJson claimTask(@RequestParam("taskId") String taskId, HttpServletRequest request)
	{
		AjaxJson j = new AjaxJson();
		String userId = "leaderuser";
		actTaskService.claimTask(taskId, userId);
		String message = "认领成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * easyui 获取流程历史数据
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "taskHistoryList")
	public void taskHistoryList(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletRequest request, HttpServletResponse response,DataGrid dataGrid) 
	{
		JSONObject jObject = actTaskService.taskHistoryList(processInstanceId);
		responseDatagrid(response, jObject);
	}
	
	// -----------------------------------------------------------------------------------
	// 以下各函数可以提成共用部件 (Add by Quainty)
	// -----------------------------------------------------------------------------------
	public void responseDatagrid(HttpServletResponse response, JSONObject jObject) 
	{
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		try 
		{
			PrintWriter pw=response.getWriter();
			pw.write(jObject.toString());
			pw.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
