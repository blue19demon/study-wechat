$(function(){
	//step1 获取微信分享参数准备
	var appId = '';
	var timestamp = '';
	var nonceStr = '';
	var signature = '';
	 $.ajax({
         type: "POST",
         url: "/wxConfig",
         data: {
        	 'url':window.location.href
         },
         success: function(data){
        	 appId=data.appId;
        	 timestamp=data.timestamp;
        	 nonceStr=data.nonceStr;
        	 signature=data.signature;
        	 wx.config({
        			debug : true,
        			appId : appId,
        			timestamp : timestamp,
        			nonceStr : nonceStr,
        			signature : signature,
        			jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
        					'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone' ]
        		});
          }
      });
});

//step2 微信js-sdk分享功能
/**
 * @param title 分享标题
 * @param desc 分享描述
 * @param shareUrl 分享链接 [微信规定分享的链接 必须是签名时候的域名下url]
 * @param imgUrl 分享图标
 */
function doShare(title,desc,shareUrl,imgUrl){
	wx
				.ready(function() {
					/*分享到朋友圈*/
					wx
							.onMenuShareTimeline({
								title : title, // 分享标题
								desc :  desc, // 分享描述
								link :  shareUrl, // 分享链接
								imgUrl : imgUrl, // 分享图标
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
								title : title, // 分享标题
								desc : desc, // 分享描述
								link : shareUrl, // 分享链接
								imgUrl : imgUrl, // 分享图标
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
								title : title, // 分享标题
								desc : desc, // 分享描述
								link : shareUrl, // 分享链接
								imgUrl : imgUrl, // 分享图标
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
								title : title, // 分享标题
								desc : desc, // 分享描述
								link : shareUrl, // 分享链接
								imgUrl : imgUrl, // 分享图标
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
								title : title, // 分享标题
								desc : desc, // 分享描述
								link : shareUrl, // 分享链接
								imgUrl : imgUrl, // 分享图标
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
}