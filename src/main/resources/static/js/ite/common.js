//公用方法,优先调用此JS
//类别改变时查询对应的计量单位

function toUpperCase(obj)
{
    obj.value = obj.value.toUpperCase()
}
function changeUnit(index,type) {
    // 用selector来传递选择器
    $.ajax({
        url:"/itevent/api/getUnit",
        data: {type:type},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            $("#unit"+index).val(res)
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
//自动查询责任人工号
$('#userid').on('focus',function () {
    $("#department").empty();
    $("#phone").val("")
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
                    var option="";
                    option += "<option value='"+res.department+"'>"+res.department+"</option>";
                    $("#department").append(option);
                    $("#phone").val(res.telephone)
                    form.render('select');
                }
            }
        });
    }

})
//自动查询责任人姓名
$('#name').on('focus',function () {
    $("#userid").val("")
    $("#phone").val("")
    $("#department").empty();
})
$('#name').on('blur',function () {
    //console.log('移出输入框')
    var data = $('#name').val()
    if (data!=null && data!=""){
        $.ajax({
            url:"/itevent/api/getIdDep",
            type:'get',
            data: {name:data},
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success:function (res) {
                console.log(res)
                if(res==null || res==""){
                    //弹窗提示
                    layer.msg("未找到此人的信息")
                }else{  //修改信息
                    var option="";
                    for (var i=0;i<res.nums;i++){
                        option += "<option value='"+res.data[i].department+"'>"+res.data[i].department+"</option>";
                    }
                    $("#department").append(option);

                    $("#userid").val(res.data[0].userid)
                    $("#phone").val(res.data[0].telephone)
                    form.render('select');
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
                totaltype = res[0];
                $('#type'+index).append(option)
                changeBrand(index,type)
                changeUnit(index,type)

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
//类别改变时查询对应的型号  index是改变哪个型号
function changeBrand(index,type,boolean) {
    //清空原有内容
    $("#brand"+index).empty();
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
                $("#brand"+index).append(option)
                //查询数量,并重新渲染
                changeNumbers(index,type,res[0]);
                //获取物料编码
                if (boolean!=false){ //若为false不查询物料编码
                    chagneItemid(index,type,res[0]);
                }
            }
        }
    })
}
//类别改变时查询对应的库存数量
function changeNumbers(index,type,brand) {
//清空原有内容
    $("#all"+index).empty();
    $("#available"+index).empty();
    $.ajax({
        url:"/itevent/api/getNumbers",
        data: {type:type,brand:brand},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            if (res!=null && res!=""){
                $("#all"+index).val(res[0])
                $("#available"+index).val(res[1])
                // 重新渲染
                setTimeout(function () {
                    form.render('select');
                },50)
            }
        }
    })
}
//类别改变时查询对应的物理编码
function chagneItemid(index,type,brand){
    console.log("查询物料编码")
    //清空原有内容
    $("#itemid"+index).empty();
    $.ajax({
        url:"/itevent/api/getItemId",
        data: {type:type,brand:brand},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            if (res!=null && res!=""){
                $("#itemid"+index).val(res)
            }else{
                layer.msg("请直接填写物料编码", {icon: 5})
            }
        }
    })
}
//动态添加绑定事件,需要用父级以上的包含,并用children选择未来元素
//物料编码改变时,改变类别和型号  ,自定义属性
$("body").on('focus',"input[item='item']",function () {
    $(this).val("")
})
$("body").on('blur',"input[item='item']",function () {
    var id = this.id;
    var index = parseInt(id.replace(/[^0-9]/ig,""));
    var itemid = $(this).val();
    if (itemid==null||itemid==""){
        layer.msg("物料编码不能为空",{icon:5})
    }else{
        $.ajax({
            url:"/itevent/api/getTypeAndBrand",
            data: {itemid:itemid},
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success:function (res) {
                console.log(res)
                if (res!=null && res!=""){
                    //清空原有内容
                    $("#brand"+index).empty();
                    changeBrand(index,res[0],false)
                    // 重新渲染
                    setTimeout(function () {
                        //设置选择现在的type
                        $("#type"+index).val(res[0])
                        //设置选择现在的brand
                        $("#brand"+index).val(res[1])
                        form.render('select');
                    },50)
                }else{
                    layer.msg("物料不存在",{icon:5})
                }
            }
        })
    }
})
