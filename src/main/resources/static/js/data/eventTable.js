//JavaScript代码区域
layui.use(['table','element','laydate'], function(){
    var table = layui.table
        ,laydate = layui.laydate
        ,element = layui.element;
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
    //创建表格实例
    //获取token令牌
    var token = localStorage.getItem("token");
    //所获得的 tableIns 即为当前容器的实例
    var tableIns = table.render({
        elem: '#event'
        ,id:'event'  //表格唯一值
        ,height: 485
        ,toolbar: '#toolbar'
        ,url: '/itevent/api/eventTable' //数据接口
        ,page: true //开启分页
        ,headers: {token: token}  //token
        ,limits:[10,50,500,1000]
        ,limit: 10
        ,cols: [[ //表头
        {field: 'uuid', title: '事件编号', width:193, sort: true, fixed: 'left'}
        ,{field: 'event', title: '事件类型', width: 126,sort: true}
        ,{field: 'itemid', title: '物料编码', width:138,sort: true}
        ,{field: 'count', title: '数量', width:70,sort: true}
        ,{field: 'unit', title: '单位', width:70,sort: true}
        ,{field: 'userid', title: '发起人工号', width:113,sort: true}
        ,{field: 'phone', title: '联系方式', width:113,sort: true}
        ,{field: 'adminuser', title: '操作人员', width:96,sort: true}
        ,{field: 'applydate', title: '申请时间', width:125}
        ,{field: 'date', title: '事件发生时间', width: 125,sort: true}
        ,{field: 'oaid', title: 'OA单号', width:190, sort: true}
        ,{field: 'orderid', title: '申请单号', width:190, sort: true}
    ]]
    })

    //点击搜索,开始搜索 ,此处必须要使用事件委托,
    $('body').on('click','#do_search',function(){
        // 搜索条件
        var search = $('#search').val();
        var date1 = $('#date1').val();
        var date2 = $('#date2').val();
        console.log("开始搜索")
        table.reload('event', {
            where: { //设定异步数据接口的额外参数
                search: search,
                date1:date1,
                date2:date2
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        }); //只重载数据
    });
    //导出数据
    $('body').on('click','#export',function () {
        var data = null;
        $.ajax({
            url: "/itevent/api/eventAllTable",
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success: function (res) {
                table.exportFile(tableIns.config.id,res.data);
            }
        })
    })
});