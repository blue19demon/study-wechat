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
<script src="http://res2.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
<title>微信分享落地页</title>
</head>
<body>
微信分享落地页，点击继续分享:${uuid }
</body>
<script type="text/javascript">
	wx.config({
		debug : true,
		appId : '${params.appId}',
		timestamp : '${params.timestamp}',
		nonceStr : '${params.noncestr}',
		signature : '${params.signature}',
		jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
				'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone' ]
	});
	wx
			.ready(function() {
				/*分享到朋友圈*/
				wx
						.onMenuShareTimeline({
							title : '点击继续分享', // 分享标题
							desc : '点击继续分享描述！', // 点击继续分享描述
							link : '${fenxurl}', // 分享链接
							imgUrl : '${params.domainAddr}/img/demo.jpg', // 分享图标
							success : function() {
								// 用户确认分享后执行的回调函数
								alert("您已分享");
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								alert('您已取消分享');
							},
							fail: function (res) {
		                        alert(JSON.stringify(res));
		                    }
						});
				/*分享给朋友*/
				wx
						.onMenuShareAppMessage({
							title : '点击继续分享', // 分享标题
							desc : '点击继续分享描述！', // 点击继续分享描述
							link : '${fenxurl}', // 分享链接
							imgUrl : '${params.domainAddr}/img/demo.jpg', // 分享图标
							type : 'link', // 分享类型,music、video或link，不填默认为link
							dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
							success : function() {
								// 用户确认分享后执行的回调函数
								alert("您已分享");
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								alert('您已取消分享');
							},
							fail: function (res) {
		                        alert(JSON.stringify(res));
		                    }
						});
				wx
						.onMenuShareQQ({
							title : '点击继续分享', // 分享标题
							desc : '点击继续分享描述！', // 点击继续分享描述
							link : '${fenxurl}', // 分享链接
							imgUrl : '${params.domainAddr}/img/demo.jpg', // 分享图标
							success : function() {
								// 用户确认分享后执行的回调函数
								alert("您已分享");
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								alert('您已取消分享');
							},
							fail: function (res) {
		                        alert(JSON.stringify(res));
		                    }
						});
				wx
						.onMenuShareWeibo({
							title : '点击继续分享', // 分享标题
							desc : '点击继续分享描述！', // 点击继续分享描述
							link : '${fenxurl}', // 分享链接
							imgUrl : '${params.domainAddr}/img/demo.jpg', // 分享图标
							success : function() {
								// 用户确认分享后执行的回调函数
								alert("您已分享");
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								alert('您已取消分享');
							},
							fail: function (res) {
		                        alert(JSON.stringify(res));
		                     }
						});
				wx
						.onMenuShareQZone({
							title : '点击继续分享', // 分享标题
							desc : '点击继续分享描述！', // 点击继续分享描述
							link : '${fenxurl}', // 分享链接
							imgUrl : '${params.domainAddr}/img/demo.jpg', // 分享图标
							success : function() {
								// 用户确认分享后执行的回调函数
								alert("您已分享");
							},
							cancel : function() {
								// 用户取消分享后执行的回调函数
								alert('您已取消分享');
							},
							fail: function (res) {
		                        alert(JSON.stringify(res));
		                    }
						});
			});
</script>
</html>
