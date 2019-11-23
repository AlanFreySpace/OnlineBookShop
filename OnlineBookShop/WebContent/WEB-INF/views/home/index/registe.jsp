<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>人脸注册</title>

<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
<style>
body {
	height: 100%;
	background: #213838;
	overflow: hidden;
}

canvas {
	z-index: -1;
	position: absolute;
}
</style>
<script src="js/jquery.js"></script>
<script src="js/verificationNumbers.js"></script>
<script src="js/Particleground.js"></script>
</head>

<body>

	<form action="${pageContext.request.contextPath}/registe.action" method="get">
		<dl class="admin_login">
			<dt>
				<strong>人脸识别头像录入</strong>
				<storng>您的用户名是：${registeUser }</storng>
				<em></em>
			</dt>
			<div id="media">
				<video id="video" width="350" height="200" autoplay></video>
				<canvas id="canvas" width="1000" height="800"></canvas>
			</div>
			<dd>
				<input type="button" onclick="registe()" value="立即注册" class="submit_btn" />
			</dd>

		</dl>
		<script type="text/javascript">
		
  		var canvans = document.getElementById("canvas");
  		var video = document.getElementById("video"); //获取video标签
  		var context = canvas.getContext("2d");
     	 
     	 var con  ={
  			audio:false,
  			video:{
  			width:1980,
  			height:1980,
  			}
  		};	
     	 
     	 //导航 获取用户媒体对象
  			navigator.mediaDevices.getUserMedia(con)
  			.then(function(stream){
  				try{
  					video.src = window.URL.createObjectURL(stream);
  				}catch(e){
  					video.srcObject=stream;
  				}
  				video.onloadmetadate = function(e){
  					video.play();
  				}
  			});
  		
  			function registe(){
  				
  				context.drawImage(video,0,0);
     			var imgData = canvans.toDataURL();
     	 		var imgData1 = imgData.split("base64,")[1];
     	  		var username = '${registeUser }';
  				$.ajax({
  					type:"post",
  					url:"${pageContext.request.contextPath}/cart/registe.action",
  					data: {"img":imgData1,"username":username},
  					success:function(data){
  						alert(data);
  						window.location.href='home/login';
					},error:function(msg){
          				alert("错误");
        			}
				});
			}
		</script>
	</form>
</body>
</html>
