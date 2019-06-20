$(function(){
	//第一步： 获取微信分享参数准备
	var appId = '';
	var timestamp = '';
	var nonceStr = '';
	var signature = '';
	 $.ajax({
         type: "POST",
         url: "/sell/wxConfig",
         data: {
        	 'url':window.location.href
         },
         success: function(data){
        	 appId=data.appId;
        	 timestamp=data.timestamp;
        	 nonceStr=data.noncestr;
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


var shareTitle=null;
var shareDescribe=null;
var shareUrl=null;
var shareIconUrl=null;
/**
 * 首次分享和转分享
 * @param shareType 分享类型
 */
window.shareData = {
		title : "Hutool", // 分享标题
		desc : "Hutool是Hu + tool的自造词，谐音“糊涂”，寓意，追求“万事都作糊涂观，无所谓失，无所谓得”的境界。",  // 分享描述
		link : "http://me9ggv.natappfree.cc/sell/wechat/authorize?returnUrl=http://me9ggv.natappfree.cc/sell/seller/product/list",      // 分享链接
		imgUrl : "https://cdn.jsdelivr.net/gh/looly/hutool-site/images/logo.jpg", // 分享图标
		type : 'link', // 分享类型,music、video或link，不填默认为link
		dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
		success : function() {
			alert("您已分享");
		},
		cancel : function() {
			// 用户取消分享后执行的回调函数
			alert('您已取消分享');
		},
		fail: function (res) {
            alert(JSON.stringify(res));
        },
        trigger: function (res) {
        	if(shareTitle!=null
        			&&shareDescribe!=null
        			&&shareIconUrl!=null
        			&&shareUrl!=null){
        		window.shareData.title=shareTitle;
           	    window.shareData.desc=shareDescribe;
           	    window.shareData.imgUrl=shareIconUrl;
           	    window.shareData.link=shareUrl;
        	}
        }
};
function doShare(){
	//触发分享SDK
	wx.ready(function() {
		/*分享给朋友*/
	    wx.onMenuShareAppMessage(shareData);
	    /*分享到朋友圈*/
		wx.onMenuShareTimeline(shareData);
		wx.onMenuShareQQ(shareData);
		wx.onMenuShareWeibo(shareData);
		wx.onMenuShareQZone(shareData);
	});
}
/**
 * @param title 分享标题
 * @param desc 分享描述
 * @param shareUrl 分享链接 [微信规定分享的链接 必须是签名时候的域名下url]
 * @param imgUrl 分享图标
 */
function doShare(title,desc,url,imgUrl){
    shareTitle=title;
	shareDescribe=desc;
	shareUrl=url;
	shareIconUrl=imgUrl;
	
	//触发分享SDK
	wx.ready(function() {
		/*分享给朋友*/
	    wx.onMenuShareAppMessage(shareData);
	    /*分享到朋友圈*/
		wx.onMenuShareTimeline(shareData);
		wx.onMenuShareQQ(shareData);
		wx.onMenuShareWeibo(shareData);
		wx.onMenuShareQZone(shareData);
	});
}