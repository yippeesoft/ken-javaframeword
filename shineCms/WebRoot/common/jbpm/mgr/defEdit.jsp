<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/common/path.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>${param.method eq 'add'?'发布流程':'编辑流程'}</title>
<link title="${themes}" rel="stylesheet" href="${path}r/operamasks-ui/css/${themes}/om-${themes}.css">
<link rel="stylesheet" href="${path}r/css/base.css"/>
<link title="${themes}" rel="stylesheet" href="${path}r/css/themes/${themes}/style.css"/>
<script type="text/javascript" src="${path}r/js/jquery.min.js"></script>
<script type="text/javascript" src="${path}r/operamasks-ui/js/operamasks-ui.min.js"></script>
<script type="text/javascript" src="${path}r/js/shine.js"></script>
<script type="text/javascript">
var validator;
//提交表单
function submitForm(){
	if(!validator.form())
		return;
	
	var url = "${path}jbpm/def_${param.method eq 'add'?'save':'update'}.do";
	var formData = $("#editForm").serialize();
    $.post(url,formData,function(data){
    	$.shine.showAjaxMsg(data,function(){
    		window.parent.saveSuccess();
    	});
    });
}
//点击取消按钮,关闭对话框
function cancel(){
	window.parent.closeEditDialog();
}
$(document).ready(function() {
	 // 对表单进行校验
    validator = $('#editForm').validate({
        rules : {
            'e.name' : {
    			required : true,
    			maxlength : 50
    		}, 
    		'e.defXml' : {
    			required : true
    		}
        }
    });
	$("#box").box();
});
</script>
</head>

<body>
<div id="box">
	<form id="editForm" method="post">
		<input type="hidden" name="e.defId" value="${e.defId}"/>
		<table class="simple_table" style="width:100%;">
			<tr>
				<td class="label">流程分类<span class="red">*</span>：</td>
				<td><input class="input" type="text" name="e.typeId" value="${typeId}"/></td>
			</tr>
			<tr>
				<td class="label">名称<span class="red">*</span>：</td>
				<td><input class="input" type="text" name="e.name" value="${e.name}"/></td>
			</tr>
			<tr>
				<td class="label">描述：</td>
				<td>
					<textarea name="e.description" rows="3" cols="55" style="width:350px;height:50px;">${e.description}</textarea>
				</td>
			</tr>
			<tr>
				<td class="label">定义XML<span class="red">*</span>：</td>
				<td>
					<textarea name="e.defXml" rows="6" cols="55" style="width:350px;height:100px;">${e.defXml}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="提 交" onclick="submitForm();" />
					<input type="button" value="取 消" onclick="cancel();" />
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>