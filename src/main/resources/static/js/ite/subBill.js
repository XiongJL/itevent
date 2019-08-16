//全局添加数量限制;
var limit = 20;
//全局可用初始化数组
var arr = new Array(limit)
var token = localStorage.getItem("token");
//JavaScript代码区域
var element,form,layer = null
//记录类型更改的值.
var totaltype = "";
layui.use(['form','layer','element','laydate'], function(){
    element = layui.element,
        form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate;
    //日期选择器初始化
    laydate.render({
        elem: '#orderDate'
    })
    //类别改变时查询对应的品牌型号
    form.on('select(type)',function (data) {
        //console.log(data.elem); //得到select原始DOM对象
        var id = data.elem.id
        var index = parseInt(id.replace(/[^0-9]/ig,""));
        // console.log(data.othis); //得到美化后的DOM对象
        console.log(data.value); //得到被选中的值
        totaltype= data.value
        changeBrand(index,data.value)
        changeUnit(index,data.value)
    })
    //型号改变时查询库存数量,以及物料编码
    form.on('select(brand)',function (data) {
        //console.log(data.elem); //得到select原始DOM对象
        var id = data.elem.id
        var index = parseInt(id.replace(/[^0-9]/ig,""));
        // console.log(data.othis); //得到美化后的DOM对象
        console.log(data.value); //得到被选中的值
        changeNumbers(index,totaltype,data.value)
        //获取物料编码
        chagneItemid(index,totaltype,data.value)

    })
    form.on('submit(sub)', function (data) {
        console.log($(data.form).serialize())
        //告诉后台有多少物料
        var index = document.getElementById("item-table").rows.length
        console.log(data)
        $.ajax({
            url: "/itevent/startBillEvent?index="+index+"&event=07",
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
                else if (res=="EmptyUserid") {
                    layer.msg("用户名不能为空")
                }else if (res.indexOf("数量不足")!=-1) {
                    layer.msg(res)
                }else if (res=="ok"){
                    layer.msg("操作成功",{icon: 1})
                }else{
                    layer.msg(res)
                }
                $("#oaid").val("")
            }
        })
        return false;
    });

});
$(function () {
    //添加导航栏选中样式
    $("#subBill").addClass("layui-this");
    $("#eventManager-nav").removeClass("layui-nav-itemed");
    $("#assets-nav").addClass("layui-nav-itemed");
    //查询所有类型,并重新渲染Select
    setTimeout(function () {
        allTypes(1);
    },500)
    //赋值初始化数值
    for(var i=1; i<=limit;i++){
        arr[i-1] = i ;
    }
})

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
        document.getElementById("item-table").insertAdjacentHTML("beforeend",' <tr>\n' +
            '                            <td>\n' +
            '                                <a onclick="addItem()" class="addItem" href="#"><i class="layui-icon layui-icon-add-1" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                                <a onclick="delItem(this)" class="delItem" href="#"><i class="layui-icon layui-icon-close" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                            </td>\n' +
            '<td><input  onkeyup="toUpperCase(this)" name="assetsid'+itemIndex+'" id="assetsid'+itemIndex+'"  class="layui-input" type="text"   placeholder="有资产牌请填写" ></td>' +
            '<td>' +
            '                                <input onkeyup="toUpperCase(this)" item="item"  name="itemid'+itemIndex+'" id="itemid'+itemIndex+'"  class="layui-input " type="text" lay-verify="required" placeholder="" >\n' +
            '                            </td>'+
            '                            <td>\n' +
            '                                <select id="type'+itemIndex+'" name="type'+itemIndex+'" lay-verify=""  lay-filter="type" lay-search></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <select id="brand'+itemIndex+'" name="brand'+itemIndex+'" lay-verify="" lay-filter="brand"></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input id="count'+itemIndex+'" name="count'+itemIndex+'" value="1" class="layui-input" type="number" min="1"   lay-verify="required" placeholder="" >\n' +
            '                            </td>\n' +
            ' <td>\n' +
            '                                <input   name="all'+itemIndex+'" id="all'+itemIndex+'" value="" class="layui-input" type="text" readonly placeholder="" >\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="available'+itemIndex+'" id="available'+itemIndex+'" value="" class="layui-input" type="text" readonly placeholder="" >\n' +
            '                            </td>' +
            '                            <td>\n' +
            '                                <input  id="unit'+itemIndex+'" name="unit'+itemIndex+'" class="layui-input" type="text"   readonly placeholder="" >\n' +
            '                            </td>\n' +
            '                        </tr>')

        allTypes(itemIndex);

        form.render('select');
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



