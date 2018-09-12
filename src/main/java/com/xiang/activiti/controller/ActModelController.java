package com.xiang.activiti.controller;


import com.xiang.activiti.service.ActModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/actModelController")
public class ActModelController
{
	@Autowired
	private ActModelService actModelService;
	
	/**
	 * 创建模型
	 */
	@RequestMapping(params = "create")
	public ModelAndView create(Model model) {
		
		return new ModelAndView("act/actModelCreate");
	}
	
	/**
	 * 创建模型页面
	 */
	@RequestMapping(params = "toCreate")
	public void create(String name, String key, HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			org.activiti.engine.repository.Model modelData = actModelService.create(name, key);
			System.out.println("11111==" + request.getContextPath());
			response.sendRedirect(request.getContextPath() + "/act/process-editor/modeler.jsp?modelId=" + modelData.getId());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
