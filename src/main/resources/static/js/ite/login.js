//JavaScript代码区域
var form,layer =null
layui.use(['form','layer'], function(){
    form = layui.form,
    layer = layui.layer;

    //自定义验证规则
    form.verify({
        pwd:[
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ]
    })



});
function login(){
    var data = $("#login").serialize()
    $.ajax({
        url: "/itevent/tologin",
        data: data,
        type: "post",
        success:function (token) {
            console.log(token)
            if (token ==null || token ==""){

            }
            else if (typeof token == "string") {  //字符串类型是token
                localStorage.setItem("token",token);
                window.location.href = "/itevent/index";
            }else{  //object 类型是错误代码
                //{code: 401, msg: "需要先登录,或用户或密码错误", data: null}
                layer.msg(token.msg)
            }
        }
    })
}
