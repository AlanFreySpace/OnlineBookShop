<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/header.jsp"%>
<div class="easyui-panel" title="添加图书" style="width:1200px;height:600px;">
	<div style="padding:0px">
<form id="add-form" method="post">
        <table cellspacing="10">
        
        	<tr>
                <td width="60" align="right">图书分类:</td>
                <td>
                	<select name="productCategoryId" idField="id" treeField="name" class="easyui-combotree easyui-validatebox"  data-options="required:true,missingMessage:'请选择图书分类'" url="tree_list" panelHeight="256px" style="width:268px">  
                    </select>
                </td>
            </tr>
        
        	<tr>
                <td width="60" align="right">图书名称:</td>
                <td><input type="text" name="name" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请填写图书名称'" /></td>
            </tr>
        
        	<tr>
                <td width="60" align="right">图书图片:</td>
                <td><input readonly="readonly" type="text" id="add-imageUrl" name="imageUrl" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请上传图片'" />
            	<a href="javascript:uploadPhoto()" id="upload-btn" class="easyui-linkbutton" iconCls="icon-upload">上传图片</a>
            	<a href="javascript:previewPhoto()" id="view-btn" class="easyui-linkbutton" iconCls="icon-eye">预览图片</a>
            	</td>
            </tr>
        
        	<tr>
                <td width="60" align="right">图书价格:</td>
                <td><input type="text" name="price" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true,min:0,precision:2,missingMessage:'请填写图书价格'" /></td>
            </tr>
            
            <tr>
                <td width="60" align="right">图书库存:</td>
                <td><input type="text" name="stock" class="wu-text easyui-numberbox easyui-validatebox" data-options="required:true,min:0,precision:0,missingMessage:'请填写图书库存'" /></td>
            </tr>
        
        	<tr>
                <td align="right">图书详情:</td>
                <td style="border:0px;"><textarea id="add-content" name="content" style="width:650px;height:210px;"></textarea></td>     
            </tr>
            
            <tr>
                <td align="right"></td>
                <td style="border:0px;">
                	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="submitForm()">提交</a>
                	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-back" onclick="back()">返回</a>
                </td>     
            </tr>
        </table>
    </form>
	</div>
</div>

<div id="preview-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
        <table>
        
        	<tr>
                <td>
                	<img src="" id="preview-photo" width="400px" >
                </td>
            </tr>
        
        </table>
</div>

<div id="process-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-upload',title:'正在上传图片'" style="width:450px;">
<div id="p" class="easyui-progressbar" style="width:400px;"></div>
</div>
<input type="file" id="photo-file" style="display:none;" onchange="upload()">

<%@include file="../common/footer.jsp"%>


	<!-- 配置文件 -->
    <script type="text/javascript" src="../../resources/admin/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="../../resources/admin/ueditor/ueditor.all.js"></script>


<script type="text/javascript">
	
	var ue = UE.getEditor('add-content');
	
	function start(){
		var value = $('#p').progressbar('getValue');
		if (value < 100){
			value += Math.floor(Math.random() * 10);
			$('#p').progressbar('setValue', value);
		}else{
			$('#p').progressbar('setValue',0);
		}
	};
	
	function upload(){
		if($("#photo-file").val()=='')return;
		var formData=new FormData();
		formData.append('photo',document.getElementById('photo-file').files[0]);
		$("#process-dialog").dialog('open');
		var interval=setInterval(start,200);
		$.ajax({
			url:'../user/upload_photo',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data){
				clearInterval(interval);
				$("#process-dialog").dialog('close');
				if(data.type=='success'){
					$("#preview-photo").attr('src',data.filepath);
					$("#add-imageUrl").val(data.filepath);
				}else{
					$.messager.alert("消息提醒",data.msg,"warning");
				}
			},
			error:function(data){
				clearInterval(interval);
				$("#process-dialog").dialog('close');
				$.messager.alert("消息提醒","上传失败!","warning");
			}
		});
	}
	
	function uploadPhoto(){
		$("#photo-file").click();
	}
	
	function previewPhoto(){
		$('#preview-dialog').dialog({
			closed: false,
			modal:true,
            title: "预览图书图片",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function(){
                	$('#preview-dialog').dialog('close');
                }
            }]
        });
	}
	
	function submitForm(){
		add();
	}
	
	function back(){
		window.history.go(-1);
	}
	
	/**
	* Name 添加记录
	*/
	function add(){
		var validate=$("#add-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		var data=$("#add-form").serialize();
		$.ajax({
			url:'add',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type=='success'){
					$.messager.alert('信息提示','添加成功！','info');
					window.history.go(-1);
				}
				else
				{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}

</script>