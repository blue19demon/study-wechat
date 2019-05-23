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
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<title>微信分享[跳转百度连接，使用外部图片]</title>
</head>
<body>
  <h1><a href="wxShreTest2">跳转page2,id=${id }</a></h1>
</body>
<script type="text/javascript" src="js/jquery-2.0.0.min.js"></script>
<!-- 引入微信的js-sdk文件 -->
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="js/wechat_share.js"></script>
<script type="text/javascript">
$(function(){
	   var title='微信分享测试 1';
		var desc='微信分享测试 11';
		//var shareUrl='http://wwww.baidu.com';
		var shareUrl='${domainAddr}/wxShreTest2?id=${id }';
		var imgUrl='http://img.zcool.cn/community/019e4d57e7e66e0000012e7e6643d9.jpg@1280w_1l_2o_100sh.jpg';
		//var imgUrl='${domainAddr}/img/demo.jpg';
		doShare(title,desc,shareUrl,imgUrl);
});
</script>
</html>