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
        laydate = layui.laydate,
        layer = layui.layer;
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
        //console.log(data.value); //得到被选中的值v
        var exchange = 0;  //是否切换的更为换的 tr, 默认0 不是,1时,2所有都更新
        if (data.elem.name.indexOf("ex")!=-1){ //name存在ex
            console.log(data.elem.name)
            exchange = 1;
        }
        totaltype= data.value
        changeBrand(index,data.value,exchange)
        changeUnit(index,data.value,exchange)
    })
    //型号改变时查询库存数量
    form.on('select(brand)',function (data) {
        //console.log(data.elem); //得到select原始DOM对象
        var id = data.elem.id
        var index = parseInt(id.replace(/[^0-9]/ig,""));
        // console.log(data.othis); //得到美化后的DOM对象
        console.log(data.value); //得到被选中的值
        var exchange = 0;  //是否切换的更为换的 tr, 默认0 不是,1时,2所有都更新
        if (data.elem.name.indexOf("ex")!=-1){ //name存在ex
            console.log(data.elem.name)
            exchange = 1;
        }
        changeNumbers(index,totaltype,data.value,exchange)
    })
    //通过layui验证输入后提交 ,需要将提交按钮放入form内
    form.on('submit(newStaff)', function (data) {
        console.log($(data.form).serialize())
        //告诉后台有多少物料
        var index = document.getElementById("item-table").rows.length
        console.log(data)
        $.ajax({
            url: "/itevent/exchangeEvent?index="+index+"&event=01",
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
    $("#oldToNew").addClass("layui-this");
    //查询所有类型,并重新渲染Select
    allTypes(1);
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
                //console.log(res)
                if(res==null || res==""){
                    //弹窗提示
                    layer.msg("未找到此工号的信息")
                }else{  //修改责任人的文本
                    $("#name").val(res.name)
                    $("#department").val(res.department)
                }
            }
        });
    }

})
//查询所有类型,并重新渲染Select
function allTypes(index) {
    console.log("开始查询所有类型")
    $.ajax({
        url:"/itevent/api/getTypes",
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            //console.log(res)
            var option="";
            var type= "";
            if (res!=null && res!=""){
                for(var x in res){
                    if (x==0){
                        type = res[x];
                    }
                    option += "<option value='"+res[x]+"'>"+res[x]+"</option>"
                }
                $('#type'+index).append(option)
                $('#extype'+index).append(option)
                changeBrand(index,type,3)
                changeUnit(index,type,3)

                // for (var i=1 ; i<itemIndex; i++){
                //     $('#type'+i).append(option)
                //     changeBrand(i,type)
                //     changeUnit(i,type)
                // }
                // 重新渲染
                setTimeout(function () {
                    form.render('select');
                },50)

            }
        }
    })

}
//类别改变时查询对应的品牌型号  index是改变哪个型号
function changeBrand(index,type,exchange) {
    //清空原有内容
    //是否为 更为换的 tr, 默认0 不是,1时,2所有都更新
    if (exchange==0){
        $("#brand"+index).empty();
    } else if (exchange==1){
        $("#exbrand"+index).empty();
    } else{
        $("#brand"+index).empty();
        $("#exbrand"+index).empty();
    }
    $.ajax({
        url:"/itevent/api/getBrands",
        data: {type:type},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            var option="";
            if (res!=null && res!=""){
                for(var x in res){
                    option += "<option value='"+res[x]+"'>"+res[x]+"</option>"
                }
                if (exchange==0){
                    $("#brand"+index).append(option)
                } else if (exchange==1){
                    $("#exbrand"+index).append(option)
                } else{
                    $("#brand"+index).append(option)
                    $("#exbrand"+index).append(option)
                }
                //查询数量,并重新渲染
                changeNumbers(index,type,res[0],exchange);
            }
        }
    })
}

//类别改变时查询对应的库存数量
function changeNumbers(index,type,brand,exchange) {
//清空原有内容
    //是否为 更为换的 tr, 默认0 不是,1时,2所有都更新
    if (exchange==0){
        $("#all"+index).empty();
        $("#available"+index).empty();
    } else if (exchange==1){
        $("#exall"+index).empty();
        $("#exavailable"+index).empty();
    } else{
        $("#all"+index).empty();
        $("#available"+index).empty();
        $("#exall"+index).empty();
        $("#exavailable"+index).empty();
    }
    $.ajax({
        url:"/itevent/api/getNumbers",
        data: {type:type,brand:brand},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            if (res!=null && res!=""){
                if (exchange==0){
                    $("#all"+index).val(res[0])
                    $("#available"+index).val(res[1])
                } else if (exchange==1){
                    $("#exall"+index).val(res[0])
                    $("#exavailable"+index).val(res[1])
                } else{
                    $("#all"+index).val(res[0])
                    $("#available"+index).val(res[1])
                    $("#exall"+index).val(res[0])
                    $("#exavailable"+index).val(res[1])
                }

                // 重新渲染
                setTimeout(function () {
                    form.render('select');
                },50)
            }
        }
    })
}
//类别改变时查询对应的计量单位
function changeUnit(index,type,exchange) {
    // 用selector来传递选择器
    $.ajax({
        url:"/itevent/api/getUnit",
        data: {type:type},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            //是否为 更为换的 tr, 默认0 不是,1时,2所有都更新
            if (exchange==0){
                $("#unit"+index).val(res)
            } else if (exchange==1){
                $("#exunit"+index).val(res)
            } else{
                $("#unit"+index).val(res)
                $("#exunit"+index).val(res)
            }
        }
    })
}
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
        document.getElementById("item-table").insertAdjacentHTML("beforeend",' <tr>\n' +
            '                            <td>\n' +
            '                                <a onclick="addItem()" class="addItem" href="#"><i class="layui-icon layui-icon-add-1" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                                <a onclick="delItem(this)" class="delItem" href="#"><i class="layui-icon layui-icon-close" style="font-size: 30px; color: #1E9FFF;"></i></a>\n' +
            '                            </td>\n' +
            '<td><input  onkeyup="toUpperCase(this)" name="assetsid'+itemIndex+'" id="assetsid'+itemIndex+'"  class="layui-input layui-col-xs1" type="text"   placeholder="有资产牌请填写" ></td>'+

            '                            <td>\n' +
            '                                <select id="type'+itemIndex+'" name="type'+itemIndex+'" lay-verify=""  lay-filter="type" lay-search></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <select id="brand'+itemIndex+'" name="brand'+itemIndex+'" lay-verify="" ></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input id="count'+itemIndex+'" name="count'+itemIndex+'" value="1" class="layui-input layui-col-xs1" type="number" min="1"   lay-verify="required" placeholder="" >\n' +
            '                            </td>\n' +
            ' <td>\n' +
            '                                <input   name="all'+itemIndex+'" id="all'+itemIndex+'" value="" class="layui-input layui-col-xs1" type="text" min="0" readonly placeholder="" >\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="available'+itemIndex+'" id="available'+itemIndex+'" value="" class="layui-input layui-col-xs1" type="text" min="0" readonly placeholder="" >\n' +
            '                            </td>' +
            '                            <td>\n' +
            '                                <input  id="unit'+itemIndex+'" name="unit'+itemIndex+'" class="layui-input layui-col-xs1" type="text"   readonly placeholder="" >\n' +
            '                            </td>\n' +
            '<td>\n' +
            '                                <input class="layui-input layui-col-xs9 " type="checkbox" name="bad'+itemIndex+'" lay-skin="switch" lay-text="是|否" value="1">\n' +
            '                            </td>'+
            '                        </tr>' +
            ' <tr>\n' +
            '                            <td>\n' +
            '                                <b>更换为</b>\n' +
            '                            </td>\n' +
            '                            <td><input  onkeyup="toUpperCase(this)"  name="exassetsid'+itemIndex+'" id="exassetsid'+itemIndex+'"  class="layui-input layui-col-xs1" type="text"  placeholder="有资产牌请填写" ></td>\n' +
            '\n' +
            '                            <td>\n' +
            '                                <select id="extype'+itemIndex+'" name="extype'+itemIndex+'" lay-verify=""  lay-filter="type" lay-search></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <select id="exbrand'+itemIndex+'" name="exbrand'+itemIndex+'" lay-verify="" ></select>\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="excount'+itemIndex+'" id="excount'+itemIndex+'" value="1" class="layui-input layui-col-xs1" type="number" min="1" lay-verify="required" placeholder="" >\n' +
            '                            </td>\n' +
            ' <td>\n' +
            '                                <input   name="exall'+itemIndex+'" id="exall'+itemIndex+'" value="" class="layui-input layui-col-xs1" type="text" readonly placeholder="" >\n' +
            '                            </td>\n' +
            '                            <td>\n' +
            '                                <input   name="exavailable'+itemIndex+'" id="exavailable'+itemIndex+'" value="" class="layui-input layui-col-xs1" type="text" readonly placeholder="" >\n' +
            '                            </td>' +
            '                            <td>\n' +
            '                                <input   name="exunit'+itemIndex+'" id="exunit'+itemIndex+'" class="layui-input layui-col-xs1" type="text"  readonly placeholder="" >\n' +
            '                            </td>\n' +
            '\n' +
            '                        </tr>')

        allTypes(itemIndex);

        form.render();
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
                $(obj).parent().parent().parent()[0].removeChild($(obj).parent().parent()[0]);//删除该tr
                //删除兄弟节点
                $("#exassetsid"+xuhao).parent().parent().parent()[0].removeChild($("#exassetsid"+xuhao).parent().parent()[0])
            }
        })
       // $(obj).parent().parent().parent()[0].removeChild($(obj).parent().parent()[0]);//删除该tr
       // $(obj).parent().parent().parent()[0].removeChild($(obj).parent().parent()[1]);//删除兄弟该tr
    }
}
function toUpperCase(obj)
{
    obj.value = obj.value.toUpperCase()
}