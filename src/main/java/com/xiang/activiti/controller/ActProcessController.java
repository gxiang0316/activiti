package com.xiang.activiti.controller;

import com.xiang.activiti.service.ActProcessService;
import com.xiang.activiti.util.HistoryProcessInstanceDiagramCmd;
import net.sf.json.JSONObject;
import org.activiti.bpmn.model.DataGrid;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;



/**
 * 流程定义相关Controller
 * @author guixiang
 * @version 2018-03-18
 */
@RestController
@RequestMapping("/actProcessController")
public class ActProcessController{

	@Autowired
	private ActProcessService actProcessService;
	
	/**
	 * 流程定义列表
	 */
	@RequestMapping(params = "processList")
	public ModelAndView processList(HttpServletRequest request) 
	{
		return new ModelAndView("jeecg/activiti/process/processlist");
	}
	
	/**
	 * 部署流程页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "toDeploy")
	public ModelAndView toDeploy(HttpServletRequest request) 
	{
		return new ModelAndView("act/actProcessDeploy");
	}

	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 */
	@RequestMapping(params = "deploy")
	public String deploy(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir, 
			String category, MultipartFile file, RedirectAttributes redirectAttributes) 
	{
		String fileName = file.getOriginalFilename();
		
		if (StringUtils.isBlank(fileName))
		{
			redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
		}
		else
		{
			String message = actProcessService.deploy(exportDir, category, file);
			redirectAttributes.addFlashAttribute("message", message);
			System.out.println("message=" + message);
		}

		return "sucess";
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
	
	// -----------------------------------------------------------------------------------
	// 以下各函数可以提成共用部件 (Add by Quainty)
	// -----------------------------------------------------------------------------------
	public void responseDatagrid(HttpServletResponse response, JSONObject jObject) {
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter pw=response.getWriter();
			pw.write(jObject.toString());
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
