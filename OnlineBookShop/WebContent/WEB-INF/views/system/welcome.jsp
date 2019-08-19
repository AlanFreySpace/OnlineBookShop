<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>欢迎！</title>
</head>
<body>
<div title="欢迎使用" style="padding:20px;overflow:hidden; color:red; " >
	<p style="font-size: 50px; line-height: 60px; height: 60px;">${admin.username}</p>
	<p style="font-size: 25px; line-height: 30px; height: 30px;">欢迎使用网上书店后台管理系统</p>
  	<p>开发人员：you</p>
  	
  	<hr />
  	<h2>系统环境</h2>
  	<p>系统环境：Windows</p>
	<p>开发工具：Eclipse</p>
	<p>Java版本：JDK 1.8</p>
	<p>服务器：tomcat 7.0</p>
	<p>数据库：MySQL 5.7</p>
	<p>系统采用技术： spring+springMVC+mybaits+EasyUI+jQuery+Ajax+面向接口编程</p>
</div>
</body>
</html>