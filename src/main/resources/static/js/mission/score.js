var token = localStorage.getItem("token");
var toUser = "";
layui.use(['form','element', 'layer','carousel','rate'], function(){
    var element = layui.element
        ,layer = layui.layer
        ,form = layui.form,
     rate = layui.rate;
  //自定义文本
    rate.render({
      elem: '#test5'
      ,value: 5
      ,text: true
      ,setText: function(value){ //自定义文本的回调
        var arrs = {
          '1': '极差'
          ,'2': '差'
          ,'3': '中等'
          ,'4': '好'
          ,'5': '极好'
        };
        this.span.text(arrs[value] || ( value + "星"));
      },choose:function(value){
    	  score=value;
      }
    })
});
var score;
$("#submit").click(function() {
	 if(score==""||score==null||score==undefined){
      	layer.msg("请对此次服务打分", {
			  icon: 2,
			  time: 1500});
      	return false;
      }
         var remark = document.getElementById("remark").value;
         $.ajax({
        		async : false,
        		type : "GET",
        		url : "/itevent/score/save",
        		dataType : "json",
        		data : {qyid:qyid,uuid:uuid,phone:phone,userid:userid,score:score,remark:remark},
             beforeSend:function(XMLHttpRequest){
             	XMLHttpRequest.setRequestHeader("token",token);
             },
             success : function(res){
             	if(res.code=="200"){
             		layer.msg(res.msg, {
           			  icon: 1
           			 });
             		setTimeout( function(){
             			window.opener=null;      //关闭页面   2秒
             			window.open("","_self");    
             			window.close();
             			}, 2000 );
             	}else if(res.code=="400"){
             		layer.msg(res.msg, {
             			  icon: 2
             			 });
             	}
             },
             error: function (err) {
             	layer.alert('数据上传失败', {
       			  icon: 2
       			 });
             }
         });    
});