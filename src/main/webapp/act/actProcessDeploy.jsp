<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp"%>
<html>
<head>
	<title>部署流程 - 流程管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#inputForm").validate({
				submitHandler: function(form){
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="actProcessController.do?list">流程管理</a></li>
		<li><a href="actProcessController.do?toDeploy">部署流程</a></li>
		<li><a href="actProcessController.do?running/">运行中的流程</a></li>
	</ul><br/>
	<sys:message content="${message}"/>
	<form id="inputForm" action="actProcessController.do?deploy" method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">流程分类：</label>
			<div class="controls">
				<select id="category" name="category" class="required input-medium">
					<option value="">全部分类</option>
					<option value="1">分类1</option>
					<option value="2">分类2</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">流程文件：</label>
			<div class="controls">
				<input type="file" id="file" name="file" class="required"/>
				<span class="help-inline">支持文件格式：zip、bar、bpmn、bpmn20.xml</span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
