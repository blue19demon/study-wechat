<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="icon" type="image/x-icon" href="/static/favicon.ico">
<title>微信授权登录结果</title>
</head>
<body>
country:${authResult.country }<br>
province:${authResult.province }<br>
city:${authResult.city }<br>
openid:${authResult.openid }<br>
sex:${authResult.sex }<br>
nickname:${authResult.nickname }<br>
headimgurl:<img alt="" src="${authResult.headimgurl }" style="width: 100px;height: 100px"><br>
language:${authResult.language }<br>
</body>
</html>