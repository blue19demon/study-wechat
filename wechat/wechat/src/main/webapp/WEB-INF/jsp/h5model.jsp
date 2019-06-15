<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="icon" type="image/x-icon" href="/static/favicon.ico">
<title>获取设备的型号</title>
<script type="text/javascript">
	// H5 plus事件处理
	function plusReady() {
		alert("Device: " + plus.device.model);
	}
	if (window.plus) {
		plusReady();
	} else {
		document.addEventListener("plusready", plusReady, false);
	}
</script>
</head>
<body>
您的手机型号为：${phone.mobileInfo}<br>
您的操作系统为：${phone.os}<br>
您的ip地址为：${phone.remoteAddr}<br>
</body>
</html>