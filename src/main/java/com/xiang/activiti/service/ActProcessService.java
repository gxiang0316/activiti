package com.xiang.activiti.service;

import net.sf.json.JSONObject;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service("actProcessService")
@Transactional
public class ActProcessService
{
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Transactional(readOnly = false)
	public String deploy(String exportDir, String category, MultipartFile file) 
	{

		String message = "";
		
		String fileName = file.getOriginalFilename();
		
		try 
		{
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) 
			{
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			}
			else if (extension.equals("png")) 
			{
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}
			else if (fileName.indexOf("bpmn20.xml") != -1)
			{
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			}
			else if (extension.equals("bpmn"))
			{ // bpmn扩展名特殊处理，转换为bpmn20.xml
				String baseName = FilenameUtils.getBaseName(fileName); 
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			}
			else
			{
				message = "不支持的文件类型：" + extension;
			}
			
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

			// 设置流程分类
			for (ProcessDefinition processDefinition : list)
			{
//				ActUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
				message += "部署成功，流程ID=" + processDefinition.getId() + "<br/>";
			}
			
			if (list.size() == 0)
			{
				message = "部署失败，没有流程。";
			}
			
		} 
		catch (Exception e) 
		{
			throw new ActivitiException("部署失败！", e);
		}
		return message;
	}
	
	/**
	 * 流程定义列表
	 */
	public JSONObject processList()
	{
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> list = query.list();
		
		StringBuffer rows = new StringBuffer();
		int i = 0;
		for(ProcessDefinition pi : list){
			i++;
			rows.append("{'id':"+i+",'processDefinitionId':'"+pi.getId() +"','startPage':'"+pi.getDescription()+"','resourceName':'"+pi.getResourceName()+"','deploymentId':'"+pi.getDeploymentId()+"','key':'"+pi.getKey()+"','name':'"+pi.getName()+"','version':'"+pi.getVersion()+"','isSuspended':'"+pi.isSuspended()+"'},");
		}
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+query.count()+",'rows':["+rowStr+"]}");
		return jObject;
	}
	
	/**
	 * 运行的流程列表
	 */
	public JSONObject runningProcessDataGrid() 
	{
		 ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
	     List<ProcessInstance> list = processInstanceQuery.list();
		
		StringBuffer rows = new StringBuffer();
		for(ProcessInstance hi : list){
			rows.append("{'id':"+hi.getId()+",'processDefinitionId':'"+hi.getProcessDefinitionId() +"','processInstanceId':'"+hi.getProcessInstanceId()+"','activityId':'"+hi.getActivityId()+"'},");
		}
		
		
		String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
		
		JSONObject jObject = JSONObject.fromObject("{'total':"+list.size()+",'rows':["+rowStr+"]}");
		return jObject;
	}
	
	public InputStream resourceRead(String processDefinitionId,
			String resourceType) 
	{
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) 
		{
			resourceName = processDefinition.getDiagramResourceName();
		}
		else if (resourceType.equals("xml")) 
		{
			resourceName = processDefinition.getResourceName();
		}
		return repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), resourceName);
	}
	
	public void delete(String deploymentId) 
	{
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
}
