//全局添加数量限制;
var limit = 20;
//全局可用初始化数组
var arr = new Array(limit)
var token = localStorage.getItem("token");
//JavaScript代码区域
var element,form,layer = null
layui.use(['form','layer','element'], function(){
    element = layui.element,
        form = layui.form,
        layer = layui.layer;
    //监听全选开关
    var all = true;
    form.on('switch(chAll)', function(data){
        console.log(data.value); //被点击的radio的value值
        if (all==false){ //全选,选中所有checkbox
            console.log($("#item-table input[type=checkbox]"))
            $("#item-table input[type=checkbox]").attr("checked",true);
            all = true
        } else{  //取消全选
            $("#item-table input[type=checkbox]").attr("checked",false);
            all = false
        }
        form.render('checkbox')
    });

    //通过layui验证输入后提交 ,需要将提交按钮放入form内
    form.on('submit(sub)', function (data) {
        console.log($(data.form).serialize())
        var data = $('#event').serialize();
        console.log(typeof data)
        //告诉后台有多少物料
        var index = document.getElementById("item-table").rows.length
        console.log(data)
        if (index<=0){
            layer.msg("此工号无使用中资产,无法提交")
            return false;
        }
        $.ajax({
            url: "/itevent/backEvent?index="+index+"&event=04",
            data: data,
            type: "post",
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success: function (res) {
                console.log(res)
                if (typeof res != "string"){
                    if (res.code == 111){
                        window.location.href = "/itevent/login";
                    }
                }
                else if (res=="EmptyUserid") {
                    layer.msg("用户名不能为空")
                }else if (res=="ok"){
                    layer.msg("操作成功",{icon: 1})
                }else{
                    layer.msg(res)
                }
            }
        });
        return false;
    });

});
$(function () {
    //添加导航栏选中样式
    $("#back").addClass("layui-this");
    //赋值初始化数值
    for(var i=1; i<=limit;i++){
        arr[i-1] = i ;
    }
})
//自动查询责任人工号
$('#userid').on('focus',function () {
    $("#name").val("")
    $("#department").val("")
})
$('#userid').on('blur',function () {
    //console.log('移出输入框')
    var data = $('#userid').val()
    if (data!=null && data!=""){
        $.ajax({
            url:"/itevent/api/getNameDepart",
            type:'get',
            data: {userid:data},
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success:function (res) {
                //清空已有内容
                $("#item-table").empty()
                //console.log(res)
                if(res==null || res==""){
                    //弹窗提示
                    layer.msg("未找到此工号的信息")
                }else{  //修改责任人的文本
                    $("#name").val(res.name)
                    $("#department").val(res.department)
                    //加载图标
                    var load = layer.load(1,{
                        shade: [0.1,'#fff']
                    })
                    //获取该用户所有使用中的资产
                    $.ajax({
                        url:"/itevent/api/whoAllUsed",
                        type:'get',
                        data: {userid:data},
                        beforeSend:function(XMLHttpRequest){
                            XMLHttpRequest.setRequestHeader("token",token);
                        },
                        success:function (res) {
                            console.log(res)
                            if(res==null || res.length<=0){
                                //弹窗提示
                                layer.msg("此工号无使用中资产,请前往资产入库")
                            }else{
                                for (var i=1;i<=res.length;i++){
                                    //添加tr列
                                    addItem(i);
                                }
                                form.render("checkbox")
                                setTimeout(function () {
                                    for (var i=1;i<=res.length;i++){
                                        //赋值
                                        $("#aid"+i).val(res[i-1].aid);
                                        $("#assetsid"+i).val(res[i-1].assetsid);
                                        $("#type"+i).val(res[i-1].type);
                                        $("#brand"+i).val(res[i-1].brand);
                                        $("#unit"+i).val(res[i-1].unit);
                                    }
                                },50)
                            }
                            layer.close(load);//关闭loading层
                        },
                        error:function () {
                            layer.close(load);//关闭loading层
                        }
                    })

                }
            }
        });

    }

})
//添加物料方法
function addItem(itemIndex) {
    //name="assetsid'+itemIndex+'"
    console.log("添加后数组"+arr);
    document.getElementById("item-table").insertAdjacentHTML("beforeend",'' +
        ' <tr>\n' +
        '                            <td style="text-align: center;">\n' +
        '                                <input type="checkbox" name="back'+itemIndex+'" title="" lay-skin="primary" checked>\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input   name="aid'+itemIndex+'" id="aid'+itemIndex+'"  class="layui-input layui-col-xs1" type="text"  >\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input   name="assetsid'+itemIndex+'" id="assetsid'+itemIndex+'"  class="layui-input layui-col-xs1" type="text"  placeholder="无" >\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input readonly id="type'+itemIndex+'" name="type'+itemIndex+'" class="layui-input layui-col-xs1" type="text" />\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input readonly id="brand'+itemIndex+'" name="brand'+itemIndex+'" class="layui-input layui-col-xs1" type="text" />\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input   readonly name="count'+itemIndex+'" id="count'+itemIndex+'" value="1" class="layui-input layui-col-xs1" type="number" min="1" lay-verify="required" placeholder="" >\n' +
        '                            </td>\n' +
        '                            <td>\n' +
        '                                <input  readonly name="unit'+itemIndex+'" id="unit'+itemIndex+'" class="layui-input layui-col-xs1" type="text"  placeholder="" >\n' +
        '                            </td>\n' +
        '                        </tr>')




}

