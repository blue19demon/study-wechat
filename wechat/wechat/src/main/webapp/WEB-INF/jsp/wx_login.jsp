<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="icon" type="image/x-icon" href="/static/favicon.ico">
<title>微信授权登录& 禁用微信分享按钮</title>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="js/jquery-2.0.0.min.js"></script>
<!-- 禁用微信分享按钮 1,解决了一个坑：IOS页面回退后再次出现分享按钮 -->
    <script>
    $(function(){  
    	    pushHistory();  
    	    window.addEventListener("popstate", function(e) {  
    	         location.reload(true);
    	     }, false);  
    	    function pushHistory() {  
    	        var state = {  
    	            title: "title",  
    	            url: "#"  
    	        };  
    	        window.history.pushState(state, "title", "#");  
    	    }  
    	}); 

       function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');
        }

        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        } 

        function closePage(){
        	WeixinJSBridge.invoke('closeWindow', {}, function (res) { alert(1)});
         }
    </script>
</head>
<body>
<a href="wxAuth?scope=snsapi_base" >微信授权登录[静默]</a><br/>
<a href="wxAuth?scope=snsapi_userinfo" >微信授权登录[弹窗]</a><br/>
<br>
<br>
<a href="javascript:closePage();" >关闭页面</a><br/>
<!-- <a href="sendTemplateMsg" >微信发送模板消息</a><br/> -->
</body>
</html>