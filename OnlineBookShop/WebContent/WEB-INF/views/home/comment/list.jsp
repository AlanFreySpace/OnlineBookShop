<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="../common/header.jsp"%>
<link rel="stylesheet" href="../resources/home/css/shop_manager.css" type="text/css" />
<link rel="stylesheet" href="../resources/home/css/shop_list.css" type="text/css" />
<style>
	.pagination{width:100%; margin:10px auto;}
	.pagination ul{float:right}
	.pagination ul li{float:left; margin:0 3px;}
	.pagination ul li span{display: inline-block; padding:5px 5px; border:1px solid #CCCCCC; color:#999999;}
	.pagination ul li span.currentpage{background-color:#FEA900; color:#FFF; font-weight: bold; border-color:#FEA900;}
</style>

<!-- 我的个人中心 -->
	<div class="shop_member_bd clearfix">
		
		<%@include file="../common/user_menu.jsp"%>
		
		<!-- 右边购物列表 -->
		<div class="shop_member_bd_right clearfix">
			
			<div class="shop_meber_bd_good_lists clearfix">
				<div class="title"><h3>我的评价列表</h3></div>
				
				<table style="font-size:12px;">
					<thead class="tab_title">
						<th style="width:80px;"><span>&nbsp;</span></th>
						<th style="width:320px;"><span>评价内容</span></th>
						<th style="width:180px;"><span>评价人</span></th>
						<th style="width:100px;"><span>宝贝信息</span></th>
						<th style="width:115px;"><span>操作</span></th>
					</thead>
					<tbody>
						
						<c:forEach items="${commentList }" var="comment">
						<tr>
							<td colspan="5">
								<table class="good" style="height:50px;font-size:12px;">
									<tbody>
										<tr>
											<td class="pingjia_pic">
												<c:if test="${comment.type==1 }">
												<span class="pingjia_type pingjia_type_1"></span>
												</c:if>
												<c:if test="${comment.type==2 }">
												<span class="pingjia_type pingjia_type_2"></span>
												</c:if>
												<c:if test="${comment.type==0 }">
												<span class="pingjia_type pingjia_type_3"></span>
												</c:if>
											</td>
											<td class="pingjia_title">
												<span>
													<a href="#">
													 	${comment.content }
													</a>
												</span><br />[<fmt:formatDate value="${comment.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />]</td>
											<td class="pingjia_danjia"><strong>${comment.account.name }</strong></td>
											<td class="pingjia_shuliang"><a href="">${comment.product.name }</a><br />${comment.product.price }元</td>
											<td class="pingjia_caozuo"><a href="javascript:void(0)" data-id="${comment.id }" class="del-btn">删除</a></td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
						</c:forEach>

					</tbody>
				</table>				
				
			<div class="pagination"> 
				<ul>
					<li><span><a href="list?page=${page-1}">上一页</a></span></li>
					<li><span class="currentpage">${page}</span></li>
					<li><span><a href="list?page=${page+1}">下一页</a></span></li>
				</ul> 
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
			$(".del-btn").click(function(){
				var $this=$(this);
				if(confirm("确定删除该评论?")){
					$.ajax({
						url:'delete',
						type:'POST',
						data:{id:$this.attr('data-id')},
						dataType:'json',
						async:false,
						success:function(data){
							if(data.type=='success'){
								window.location.reload();
							}else{
								alert(data.msg);
							}
						}
					});
				}
			});
	});
		
</script>


</html>  