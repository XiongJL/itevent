
//全局添加数量限制;
var limit = 20;
//全局可用初始化数组
var arr = new Array(limit)
var token = localStorage.getItem("token");
//JavaScript代码区域
var element,form,layer = null
layui.use(['form','layer','element','laydate'], function(){
    element = layui.element,
        form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate;
    //日期选择器初始化
    laydate.render({
        elem: '#orderDate'
    })
    //通过layui验证输入后提交 ,需要将提交按钮放入form内
    form.on('submit(sub)', function (data) {
        console.log($(data.form).serialize())
        //告诉后台有多少物料
        var index = document.getElementById("item-table").rows.length
        $.ajax({
            url: "/itevent/addItem?index="+index+"&event=09",
            data: $(data.form).serialize(),
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
                else if (res.indexOf("物料已存在")!=-1) {
                    layer.msg(res)
                }else if (res=="ok"){
                    layer.msg("操作成功",{icon: 1})
                    setTimeout(function () {
                        location.reload();
                    },2000)
                }else {
                    layer.msg(res)
                }


            }
        });
        return false;
    });

});
$(function () {
    //添加导航栏选中样式
    $("#newItem").addClass("layui-this");
    $("#eventManager-nav").removeClass("layui-nav-itemed");
    $("#assetsIn-nav").addClass("layui-nav-itemed");
    //赋值初始化数值
    for(var i=1; i<=limit;i++){
        arr[i-1] = i ;
    }
})

//获取参数序列 , 从1-20最大个数
function getParmId() {
    //获取所有tr中的第一个td中的name,查看已有序列
    console.log(arr)
    var indexs = new Array(limit);
    $('#item-table tr').each(function(i,ele){
        $(this).find('td').each(function(index,ele){
            if (index==1){
                var name = $(ele).find('input')[0].name;
                var xuhao = parseInt(name.replace(/[^0-9]/ig,""));
                indexs[i] = xuhao;
            }
        })
    })
    // console.log(indexs)
    //获取不重复的数组
    for (var i = 0;i<arr.length;i++){
        for (var j = 0 ; j<indexs.length;j++){
            // console.log(arr[i]+"---"+indexs[j])
            if (arr[i]==indexs[j]){
                arr.splice(i,1)
            }
        }
    }
    // console.log("去重后的数组"+arr)
    var ranId = Math.floor((Math.random()*arr.length))
    var num = arr[ranId];
    arr.splice(ranId,1)
    //  console.log("从去重中随机: "+num)
    //随机抽取
    return num
}

//点击添加物料
function addItem() {
    //所有tr的数量
    var trNum = document.getElementById("item-table").rows.length
    if (trNum>=limit){
        layer.msg("不能再添加了")
    } else{
        //获取参数序列
        var itemIndex = getParmId();
        console.log("添加后数组"+arr);
        document.getElementById("item-table").insertAdjacentHTML("beforeend",'<tr>\n' +
            '                            <td>\n' +
            '                                <a class="addItem" href="#" onclick="addItem()"><i class="layui-icon layui-icon-add-1" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                                <a class="delItem" href="#" onclick="delItem(this)"><i class="layui-icon layui-icon-close" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                            </td>\n' +
            '                            <td><input  onkeyup="toUpperCase(this)" name="itemid'+itemIndex+'" id="itemid'+itemIndex+'" lay-verify="required" class="layui-input layui-col-xs1" type="text"  placeholder="必填" ></td>\n' +
            '                            <td>\n' +
            '                                <input   name="type'+itemIndex+'" id="type'+itemIndex+'" lay-verify="required" class="layui-input layui-col-xs1" type="text"  placeholder="必填" >\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="brand'+itemIndex+'" id="brand'+itemIndex+'"  lay-verify="required"class="layui-input layui-col-xs1" type="text"  placeholder="必填" >\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="unit'+itemIndex+'" id="unit'+itemIndex+'"lay-verify="required" class="layui-input layui-col-xs1" type="text"   placeholder="必填" >\n' +
            '                            </td>\n' +
            '                        </tr>')

    }

}
//点击删除物料
function delItem(obj) {
    var trNum = document.getElementById("item-table").rows.length
    // console.log("tr数量有"+trNum)
    if (trNum>1){
        console.log("原数组:"+arr)
        var a = $(obj).parent().parent().find("td").eq(1).find("input")[0].name;
        console.log(a) // 获取删除按钮的td的兄弟节点的input的name属性值.children[1].find('input')[0].name
        $(obj).parent().parent().find('td').each(function(index,ele){
            if (index==1){
                var name = $(ele).find('input')[0].name;
                var xuhao = parseInt(name.replace(/[^0-9]/ig,""));
                //在全局数组中添加该序号值
                arr.push(xuhao)
                console.log("现数组:"+arr)
            }
        })

        $(obj).parent().parent().parent()[0].removeChild($(obj).parent().parent()[0]);//删除该tr

    }
}


function toUpperCase(obj)
{
    obj.value = obj.value.toUpperCase()
}