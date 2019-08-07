var table ;
//JavaScript代码区域
layui.use(['table','layer','element','laydate'], function(){
        table = layui.table
        ,laydate = layui.laydate
        ,layer = layui.layer
        ,element = layui.element;
    //创建表格实例
    //获取token令牌
    var token = localStorage.getItem("token");
    //初始化日期
    laydate.render({
        elem:'#date1'
        ,min: '2017-1-1'
        ,max: '2029-12-31'
    });
    laydate.render({
        elem:'#date2'
        ,min: '2017-1-1'
        ,max: '2029-12-31'
    });
    //所获得的 tableIns 即为当前容器的实例
    var tableIns = table.render({
        elem: '#assets'
        ,height: 485
        ,toolbar: '#toolbar'
        ,url: '/itevent/api/assetsTable' //数据接口
        ,page: true //开启分页
        ,headers: {token: token}  //token
        ,limits:[10,50,500,1000]
        ,limit: 10
        ,cols: [[ //表头
        {field: 'aid', title: 'ID', width:60, sort: true, fixed: 'left'}
        ,{field: 'assetsid', title: '资产牌', width:120,sort: true}
        ,{field: 'username', title: '使用人', width:100,sort: true}
        ,{field: 'userid', title: '工号', width:116,sort: true}
        ,{field: 'phone', title: '联系方式', width:116,sort: true}
        ,{field: 'type', title: '物料名称', width: 177,sort: true}
        ,{field: 'brand', title: '物料描述', width: 267,sort: true}
        ,{field: 'state', title: '状态', width:80, sort: true}
        ,{field: 'store', title: '所在仓', width:80, sort: true}
        ,{field: 'buyDate', title: '创建日期', width:125}
        ,{fixed: 'right', title:'操作', toolbar: '#bar', width:124}
    ]]
    })

    //监听行工具事件 , 删除
    table.on('tool(assets)', function(obj){
        var data = obj.data;
        console.log(data.aid)
        if(obj.event === 'del'){
            layer.confirm('真的删除么', function(index){
                $.ajax({
                    url:"/itevent/delAssets",
                    data:{id:data.aid},
                    beforeSend:function(XMLHttpRequest){
                        XMLHttpRequest.setRequestHeader("token",token);
                    },
                    success:function (res) {
                        if (typeof res != "string"){
                            if (res.code == 111){
                                window.location.href = "/itevent/login";
                            }
                        }else if(res == "ok"){
                            layer.msg("删除成功",{icon: 1})
                            obj.del();
                            layer.close(index);
                        }else if(res =="Assets null"){
                            layer.msg("资产不存在")
                        }
                        else{
                            layer.msg(res)
                        }
                    }
                })
            });
        }
        else if(obj.event === 'edit'){
            layer.prompt({
                formType: 2
                ,value: data.email
            }, function(value, index){
                obj.update({
                    email: value
                });
                layer.close(index);
            });
        }
    });

    //点击搜索,开始搜索 ,此处必须要使用事件委托,
    $('body').on('click','#do_search',function(){
        // 搜索条件
        var search = $('#search').val();
        var date1 = $('#date1').val();
        var date2 = $('#date2').val();
        console.log("开始搜索")
        table.reload('assets', {
            where: { //设定异步数据接口的额外参数，任意设
                //他是将where中的数据 以key value的形式传递的,如assetsTable?page=1&limit=10&search=xxx&bbb=yyy
                search: search,
                date1:date1,
                date2:date2
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        }); //只重载数据
    });



    //获取即将逾期资产
    if (window.location.href.indexOf("yuqi")!=-1){
        setTimeout(function () {
            table.reload('assets', {
                    where: {
                        search: "逾期资产",
                        date1:null,
                        date2:null
                    }
                    ,page: {
                        curr: 1 //重新从第 1 页开始
                    }
                }); //只重载数据
        },500)
    }
});

