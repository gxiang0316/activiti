package com.xiang.activiti.act.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xiang.activiti.act.entity.CusModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/model")
public class ActModelController
{

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
		return "actModelCreat";
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
			System.out.println("创建模型失败：");
		}
	}
}
