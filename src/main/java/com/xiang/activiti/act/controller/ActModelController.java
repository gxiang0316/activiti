package com.xiang.activiti.act.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xiang.activiti.act.entity.CusModel;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/model")
public class ActModelController
{
	public static final Logger LOG =LoggerFactory.getLogger(ActModelController.class);

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ProcessEngine processEngine;
	
	/**
	 * 新建模型
	 */
	@RequestMapping(value = "/newModel",method = RequestMethod.GET)
	public String newModel(ModelMap map) {
		map.put("model",new CusModel());
		return "act/model/actModelCreat";
	}
	
	/**
	 * 创建模型
	 */
	@PostMapping(value = "/create")
	public void create(@ModelAttribute CusModel model, HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Model modelData = repositoryService.newModel();
			//设置一些默认信息
			String name = model.getName();
			String description = model.getDescription();
			int revision = 1;
			String key = model.getKey();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(key);
			//保存模型
			repositoryService.saveModel(modelData);
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
		}
		catch (Exception e)
		{
			LOG.error("新建模型失败：", e);
		}
	}

	/**
	 * 编辑模型
	 */
	@RequestMapping(value = "edit/{modelId}")
	public void create(@PathVariable("modelId")String modelId,HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelId);
		}
		catch (Exception e)
		{
			LOG.error("编辑模型失败：modelId={}", modelId, e);
		}
	}

	/**
	 * 部署模型
	 * @param modelId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deploy/{modelId}")
	@ResponseBody
	public String deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes)
	{
		try {
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			String sxss = new String(bpmnBytes,"UTF-8");

			String processName = modelData.getName() + ".bpmn20.xml";

			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName()).addString(processName, new String(bpmnBytes,"UTF-8"))
					.deploy();
			redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
		} catch (Exception e)
		{
			LOG.error("根据模型部署流程失败：modelId={}", modelId, e);
			//return "act/model/actModelList";
			return "false";
		}
		//return "act/model/actModelList";
		return "success";
	}

	@RequestMapping(value = "queryModels")
	public String modelList(ModelMap model)
	{
		List<Model> result = repositoryService.createModelQuery().list();
		model.addAttribute("modelList",result);
		return "act/model/actModelList";
	}









}
