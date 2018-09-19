package com.xiang.activiti.act.controller;

import com.xiang.activiti.act.service.ActProcessService;
import com.xiang.activiti.act.util.HistoryProcessInstanceDiagramCmd;
import net.sf.json.JSONObject;
import org.activiti.bpmn.model.DataGrid;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;


/**
 * 流程定义相关Controller
 * @author guixiang
 * @version 2018-03-18
 */
@Controller
@RequestMapping("/process")
public class ActProcessController{

	@Autowired
	private ActProcessService actProcessService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * 流程管理
	 */
	@RequestMapping(value = "/toProcess")
	public String toProcess()
	{
		return "actProcess";
	}

	/**
	 * 部署流程页面跳转
	 *
	 * @return
	 */
	@GetMapping(value = "/toDeploy")
	public String toDeploy()
	{
		return "actProcessDeploy";
	}

	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 */
	@PostMapping(value = "/deploy")
	@ResponseBody
	public String deploy(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
	{
		String fileName = file.getOriginalFilename();

		if (StringUtils.isBlank(fileName))
		{
			redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
		}
		else
		{
			String message = actProcessService.deploy(file);
			redirectAttributes.addFlashAttribute("message", message);
			System.out.println("message=" + message);
		}

		return "sucess";
	}

	
	/**
	 * 流程定义列表
	 */
	@RequestMapping(value = "/processList")
	public String processList(Model model)
	{
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		model.addAttribute("processList",list);
		return "actProcessList";
	}

	/**
	 * 启动流程实例
	 */
	public String startProcess(@PathVariable("id")String id)
	{
		runtimeService.startProcessInstanceByKey(id);
		return "";
	}

	/**
	 * easyui 运行中流程列表页面
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "runningProcessList")
	public ModelAndView runningProcessList(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) 
	{
		return new ModelAndView("jeecg/activiti/process/runninglist");
	}
	
	/**
	 * easyui 运行中流程列表数据
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "runningProcessDataGrid")
	public void runningProcessDataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
	{
		JSONObject jObject = actProcessService.runningProcessDataGrid();
		responseDatagrid(response, jObject);
	}
	
	/**
	 * 读取工作流定义的图片或xml
	 * @throws Exception
	 */
	@RequestMapping(params = "resourceRead")
    public void resourceRead(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception 
                                 {
        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, resourceType);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1)
        {
            response.getOutputStream().write(b, 0, len);
        }
    }
	
	/**
	 * 读取带跟踪的流程图片
	 * @throws Exception
	 */
	@RequestMapping(params = "traceImage")
    public void traceImage(@RequestParam("processInstanceId") String processInstanceId,
    		HttpServletResponse response) throws Exception {
    	
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); 
        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        
        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
        	response.getOutputStream().write(b, 0, len);
        }
    }

	/**
	 * 流程历史页面
	 * @param processInstanceId
	 * @param request
	 * @param respone
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "viewProcessInstanceHistory")
	public ModelAndView viewProcessInstanceHistory(@RequestParam("processInstanceId") String processInstanceId,
			HttpServletRequest request, HttpServletResponse respone,Model model) 
	{
		model.addAttribute("processInstanceId", processInstanceId);
		return new ModelAndView("jeecg/activiti/process/viewProcessInstanceHistory");
	}
	
	
	
	
	/**
     * 删除部署的流程，级联删除流程实例
     * @param deploymentId 流程部署ID
     */
//	@RequestMapping(params = "del")
//	@ResponseBody
//	public AjaxJson del(@RequestParam("deploymentId") String deploymentId, HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		actProcessService.delete(deploymentId);
//		String message = "删除成功";
//		j.setMsg(message);
//		return j;
//	}
	
	/**
	 * easyui AJAX请求数据
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid)
	{
		JSONObject jObject = actProcessService.processList();
		responseDatagrid(response, jObject);
	}
	

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
