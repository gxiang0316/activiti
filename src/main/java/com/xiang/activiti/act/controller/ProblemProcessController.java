package com.xiang.activiti.act.controller;

import com.xiang.activiti.act.entity.Leave;
import com.xiang.activiti.act.entity.ProblemProcess;
import com.xiang.activiti.act.entity.Variable;
import com.xiang.activiti.act.service.LeaveService;
import com.xiang.activiti.act.service.ProblemProcessService;
import com.xiang.activiti.act.util.AjaxJson;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/problem")
public class ProblemProcessController {
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private ProblemProcessService problemProcessService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
    protected TaskService taskService;

	private static final Logger logger = Logger.getLogger(ProblemProcessController.class);

	/**
	 * 新建工单,启动流程实例表单
	 */
	@RequestMapping(value = "/createProcess")
	public String startPageSelect(@RequestParam("startPage") String startPage,@RequestParam("key")String key, Model model)
	{
		model.addAttribute("key",key);
		return "act/problemProcess/"+startPage.substring(0, startPage.lastIndexOf("."));
	}

	/**
     * 流程启动
     * @param problemProcess
     */
	@RequestMapping(value = "startProcess")
	@ResponseBody
	public AjaxJson startProcess(ProblemProcess problemProcess, HttpServletRequest request)
	{
		AjaxJson j = new AjaxJson();

		problemProcess.setCreator("admin");
		
		//请假流程启动
		problemProcessService.startProcess(problemProcess);
		
		String message = "流程启动成功";
		j.setMsg(message);
		return j;
	}


	
	/**
	 * 完成任务表单选择，即每一环节的表单选择
	 */
	@RequestMapping(value = "taskCompletePageSelect")
	public String taskCompletePageSelect(@RequestParam("taskPage") String taskPage,
			@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("taskId") String taskId,HttpServletRequest request,Model model) 
	{
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
		String businessKey = processInstance.getBusinessKey();
		ProblemProcess problemProcess = problemProcessService.getProblemProcess(businessKey);
		model.addAttribute("processInstanceId", processInstanceId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("problemProcess",problemProcess);
		//System.out.println(jspPage);
		return "act/problemProcess/"+taskPage.substring(0, taskPage.lastIndexOf("."));
	}

	/**
	 * 完成任务
	 * @param taskId
	 * @param request
	 * @return
	 */
	@PostMapping(value = "completeTask")
	@ResponseBody
	public AjaxJson completeTask(String taskId,HttpServletRequest request)
	{
		AjaxJson j = new AjaxJson();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("dispatch",true);
		variables.put("handleSucess",false);
        taskService.complete(taskId, variables);
		String message = "处理成功";
		j.setMsg(message);
		return j;
	}
	
}
