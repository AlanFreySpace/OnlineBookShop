<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/header.jsp"%>
<link rel="stylesheet" href="../resources/home/css/shop_manager.css" type="text/css" />
<link rel="stylesheet" href="../resources/home/css/shop_form.css" type="text/css" />

<!-- 我的个人中心 -->
	<div class="shop_member_bd clearfix">
		
		<%@include file="../common/user_menu.jsp"%>
		
		<!-- 右边购物列表 -->
		<div class="shop_member_bd_right clearfix">
			
			<div class="shop_meber_bd_good_lists clearfix">
				<div class="title"><h3>修改密码</h3></div>
				<div class="clear"></div>
				<div class="shop_home_form">
					<form athion="" name="" class="shop_form" method="post">
						<ul>
							<li class="bn"><label>原密码：</label><input id="old-pwd" type="password" class="truename form-text" /></li>
							<li class="bn"><label>新密码：</label><input id="new-pwd" type="password" class="truename form-text" /></li>
							<li class="bn"><label>重复新密码：</label><input id="new-pwd-re" type="password" class="truename form-text" /></li>
							<li class="bn"><label>&nbsp;</label><input type="button" onclick="updatePwd()" class="form-submit" value="保存修改" /></li>
						</ul>
					</form>
				</div>
			</div>
		</div>
		</div>
		<!-- 右边购物列表 End -->

	</div>
	<!-- 我的个人中心 End -->
	
	
	
	<!-- Footer - wll - 2013/3/24 -->
	<div class="clear"></div>
    <%@include file="../common/footer.jsp"%>	
</body>
<script>
	
	$(document).ready(function(){
			
	});
	
	function updatePwd(){
		var password=$("#old-pwd").val();
		var newPassword=$("#new-pwd").val();
		var newPasswordre=$("#new-pwd-re").val();
		if(password==''){
			alert('请填写原密码');
			return;
		}
		if(newPassword==''){
			alert('请填写新密码');
			return;
		}
		if(newPassword!=newPasswordre){
			alert('两次输入密码不一致');
			return;
		}
		$.ajax({
			url:'update_pwd',
			type:'POST',
			data:{password:password,newPassword:newPassword},
			dataType:'json',
			async:false,
			success:function(data){
				if(data.type=='success'){
					alert('更新成功');
					//window.location.href='index';
				}else{
					alert(data.msg);
				}
			}
		});
	}
</script>


</html>  