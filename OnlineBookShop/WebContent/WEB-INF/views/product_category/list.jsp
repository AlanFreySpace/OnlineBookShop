<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="wu-toolbar">
        <div class="wu-toolbar-button">
        	<%@include file="../common/menus.jsp"%>
        </div>
        <div class="wu-toolbar-search">
            <label>图书分类：</label><input id="search-name" class="wu-text" style="width:100px">
            
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-treegrid" toolbar="#wu-toolbar"></table>
</div>



<!-- Begin of easyui-dialog -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="add-form" method="post">
        <table>
        
        	<tr>
                <td width="60" align="right">父分类:</td>
                <td>
                	<select name="parentId" idField="id" treeField="name" class="easyui-combotree" url="tree_list" panelHeight="256px" style="width:268px">
            	       
                    </select>
                </td>
            </tr>
        
        	<tr>
                <td width="60" align="right">分类名称:</td>
                <td><input type="text" name="name" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请填写分类名称'" /></td>
            </tr>
        
        	<tr>
                <td align="right">备注:</td>
                <td><textarea id="add-remark" name="remark" rows="6" class="wu-textarea" style="width:260px"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->

<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="edit-form" method="post">
		<input type="hidden" name="id" id="edit-id">
        <table>
        
        	<tr>
                <td width="60" align="right">父分类:</td>
                <td>
                	<select id="edit-parentId" name="parentId" idField="id" treeField="name" class="easyui-combotree" url="tree_list" panelHeight="256px" style="width:268px">
            	       
                    </select>
                </td>
            </tr>
        
        	<tr>
                <td width="60" align="right">分类名称:</td>
                <td><input type="text" id="edit-name" name="name" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请填写分类名称'" /></td>
            </tr>
        
        	<tr>
                <td align="right">备注:</td>
                <td><textarea id="edit-remark" name="remark" rows="6" class="wu-textarea" style="width:260px"></textarea></td>
            </tr>
            
        </table>
    </form>
</div>

<%@include file="../common/footer.jsp"%>

<script type="text/javascript">
	
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
					$('#add-dialog').dialog('close');
					$('#data-datagrid').treegrid('reload');
				}
				else
				{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	
	function edit(){
		var validate=$("#edit-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		var data=$("#edit-form").serialize();
		$.ajax({
			url:'edit',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type=='success'){
					$.messager.alert('信息提示','编辑成功！','info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').treegrid('reload');
				}
				else
				{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	
	
	
	/**
	* Name 删除记录
	*/
	function remove(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#data-datagrid').datagrid('getSelected');
				if(item==null||item.length==0){
					$.messager.alert('信息提示','请选择要删除的数据！','info');	
					return;
				}
				$.ajax({
					url:'delete',
					dataType:'json',
					type:'post',
					data:{id:item.id},
					success:function(data){
						if(data.type=='success'){
							$.messager.alert('信息提示','删除成功！','info');
							$('#data-datagrid').treegrid('reload');
						}
						else
						{
							$.messager.alert('信息提示',data.msg,'warning');
						}
					}
				});
			}	
		});
	}
	
	/**
	* Name 打开添加窗口
	*/
	function openAdd(){
//		$('#add-form').form('clear');
		$('#add-dialog').dialog({
			closed: false,
			modal:true,
            title: "添加图书分类信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            //	$("#add-form input").val('');
            }
        });
	}
	
	
	
	function openEdit(){
//		$('#add-form').form('clear');
		var item = $('#data-datagrid').datagrid('getSelected');
		if(item==null||item.length==0){
			$.messager.alert('信息提示','请选择要编辑的数据！','info');	
			return;
		}
		$('#edit-dialog').dialog({
			closed: false,
			modal:true,
            title: "编辑图书分类信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            	//	$("#add-form input").val('');
            	$("#edit-id").val(item.id);
            	$("#edit-parentId").combotree('setValue',item.parentId);
            	$("#edit-name").val(item.name);
            	$("#edit-remark").val(item.remark);
            }
        });
	}
	
	
	
	$("#search-btn").click(function(){
		var option={name:$("#search-name").val()};
		$('#data-datagrid').treegrid('reload',option);
	});
	
	
	function add0(m){return m<10?'0'+m:m }
	function format(shijianchuo){
	//shijianchuo是整数，否则要parseInt转换
		var time = new Date(shijianchuo);
		var y = time.getFullYear();
		var m = time.getMonth()+1;
		var d = time.getDate();
		var h = time.getHours();
		var mm = time.getMinutes();
		var s = time.getSeconds();
		return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
	}
	
	/**
	* Name 载入数据
	*/
	$('#data-datagrid').treegrid({
		url:'list',	
		rownumbers:true,
		singleSelect:true,
		pageSize:20,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		idField:'id',
		treeField:'name',
		fit:true,
		columns:[[
			{ field:'chk',checkbox:true},
			{ field:'name',title:'图书分类名称',width:150,sortable:true},
			{ field:'remark',title:'备注',width:200}
		]],
		onLoadSuccess: function (row, data)
        {
            $.each(data, function (i, val) { $('#data-datagrid').treegrid('collapseAll', data[i].id)})
        }
	});
</script>