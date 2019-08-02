var table ;
//JavaScript代码区域
layui.use(['table','element','laydate'], function(){
        table = layui.table
        ,laydate = layui.laydate
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
    ]]
    })



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

    //导出数据
    $('body').on('click','#export',function () {
        var data = null;
        $.ajax({
            url: "/itevent/api/assetsAllTable",
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success: function (res) {
                table.exportFile(tableIns.config.id,res.data);
            }
        })
    })

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

