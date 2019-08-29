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
                $.ajax({  //获取用户之前打算访问的页面
                    url:'/itevent/originURL',
                    //   https://mesqrcode.liwinon.com/itevent/login
                    success:function (res) {
                        var url = "/itevent/index";
                        if (res!=null && res!="" && res.indexOf("login")==-1){ //如果第一次进入的是登录页面不跳转回去
                            if (res.indexOf("mesqrcode")!=-1){
                                var tmp ="";
                                var urls =  res.split("/");
                                if (urls.length>2){
                                    for(var index=3;index<urls.length;index++){
                                        tmp += "/"+urls[index]
                                    }
                                }
                                console.log("tmp:"+tmp)
                                url = tmp;
                            }
                        }
                        console.log("修改后的路径:"+url)
                        window.location.href = url;
                    }
                })
            }else{  //object 类型是错误代码
                //{code: 401, msg: "需要先登录,或用户或密码错误", data: null}
                layer.msg(token.msg)
            }
        }
    })
}
