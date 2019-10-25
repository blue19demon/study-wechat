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
        			jsApiList : [ 'getLocation' ]
        		});
        	 
        	 wx.ready(function () {
        		    wx.checkJsApi({
        		        jsApiList: [
        		            'getLocation'
        		        ],
        		        
        		        success: function (res) {
        		            if (res.checkResult.getLocation == false) {
        		                alert('你的微信版本太低，不支持微信JS接口，请升级到最新的微信版本！');
        		                return;
        		            }
        		        }
        		    }); 
        		    wx.error(function(res){
        		        alert("接口调取失败")
        		    });
        		    wx.getLocation({
        		      type: 'gcj02',
        		      success: function (res) {
        		       
        		        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
        		        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
        		        $.ajax({
        		        	   url: './mapLocation?latitude='+latitude+"&longitude="+longitude,
        		        	   type: "GET",
        		        	   contentType : "application/json",
        		        	   async : false,
        		        	   success: function(data){
        		        		  alert(data);
        		        	   },
				        		error: function (jqXHR, textStatus, errorThrown) {
					        	/*错误信息处理*/
					        	 alert(textStatus+','+errorThrown);
				        	}
        		        });
        		      },
        		      cancel: function (res) {
        		        alert('用户拒绝授权获取地理位置');
        		      }
        		    });
        		});
          }
      });
});
