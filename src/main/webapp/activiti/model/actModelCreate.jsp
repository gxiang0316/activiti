<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>新建模型</title>
<t:base type="jquery,easyui,tools"></t:base>
<script type="text/javascript">
		$(document).ready(function(){
			$("#inputForm").validate({
				submitHandler: function(form)
				{
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		function submitForm()
		{
			
		}
		
	</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" layout="table" action="actModelController.do?toCreate">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="15%" nowrap><label class="Validform_label">流程名称: </label></td>
			<td class="value" width="85%">
				<input id="name" class="inputxt" name="name" value="" datatype="s2-20" />			
			</td>
		</tr>
		<tr>
			<td align="right" width="15%" nowrap><label class="Validform_label">KEY：</label></td>
			<td class="value" width="85%">
				<input id="key" class="inputxt" name="key" value="" datatype="s2-20" />				
			</td>
		</tr>
		
	</table>
	<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交" onclick="submitForm();" />
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
</t:formvalid>
</body>
	