package com.xiang.activiti.act.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service("actModelService")
@Transactional
public class ActModelService
{
	@Autowired
	private RepositoryService repositoryService;
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException 
	 */
	@Transactional(readOnly = false)
	public Model create(String name, String key) throws UnsupportedEncodingException 
	{
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_author", "jeecg");
		editorNode.put("properties", properties);
		ObjectNode stencilset = objectMapper.createObjectNode();
		stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilset);
		Model modelData = repositoryService.newModel();
		String description = StringUtils.defaultString("test");
		modelData.setKey(StringUtils.defaultString(key));
		modelData.setName(name);
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());
		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		return modelData;
	}
}
