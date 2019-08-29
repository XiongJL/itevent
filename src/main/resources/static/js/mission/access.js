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
      ,value: 3
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
    	  alert(value);
      }
    })
});