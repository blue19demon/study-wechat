<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>确认支付</title>
</head>
<body>
    <input type="text" id="appId" value="${appId}">
    <input type="text" id="nonceStr" value="${nonceStr}">
    <input type="text" id="prepayId" value="${prepayId}">
    <input type="text" id="paySign" value="${paySign}">
    <input type="text" id="timeStamp" value="${timeStamp}">
</body>
<script type="text/javascript" src="http://mat1.gtimg.com/libs/jquery/1.12.0/jquery.js"></script> 
<script>
 
    function onBridgeReady(){
        var appId = document.getElementById("appId").value;
        var nonceStr = document.getElementById("nonceStr").value;
        var prepayId = document.getElementById("prepayId").value;
        var paySign = document.getElementById("paySign").value;
        var timeStamp = document.getElementById("timeStamp").value;
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId":appId,
                    "timeStamp":timeStamp,
                    "nonceStr":nonceStr,
                    "package":prepayId,
                    "signType":"MD5",
                    "paySign":paySign
                },
            function(res){
                if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                    alert("ok");
                }else {//这里支付失败和支付取消统一处理
                    alert("支付取消");
                }
            }
        );
    }
 
    window.onload=function () {
        if (typeof WeixinJSBridge == "undefined"){
            if (document.addEventListener){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
            onBridgeReady();
        }else {
            onBridgeReady();
        }
    }
</script>
</html>
 
