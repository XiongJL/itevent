var token = localStorage.getItem("token");
var toUser = "";
layui.use(['form','element', 'layer','carousel'], function(){
    var element = layui.element
        ,layer = layui.layer
        ,form = layui.form;

    //监听折叠
    element.on('collapse(test)', function(data){
        // layer.msg('展开状态：'+ data.show);
    });
    layer.photos({
        photos: '#photos'
        ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
    });

});