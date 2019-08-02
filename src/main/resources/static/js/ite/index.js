//获取token
var token = localStorage.getItem("token");
//JavaScript代码区域
layui.use(['element','layer'], function(){
    var element = layui.element,
        layer = layui.layer;

});
$(function () {
    //默认查询 selected 的年份
    var year = $("#year").val();
    getData(year)
    $("#year").on('change',function () {
        getData($(this).val());
    })
})

//初始化图表
var chart1 = echarts.init(document.getElementById('chart1'));
//异步获取数据
function getData(year) {
    //异步请求数据
    chart1.showLoading();
    $.ajax({
        url:"/itevent/api/chart1",
        data: {year:year},
        type: "get",
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success: function (res) {
            chart1.hideLoading();
            console.log(res);
            if (res!=null){
                //设置配置
                chart1.setOption({
                    legend: {},
                    tooltip: {},
                    dataset: {
                        source: [
                            ['product',year],
                            ['一月', res[1]],
                            ['二月', res[2]],
                            ['三月', res[3]],
                            ['四月', res[4]],
                            ['五月', res[5]],
                            ['六月', res[6]],
                            ['七月', res[7]],
                            ['八月', res[8]],
                            ['九月', res[9]],
                            ['十月', res[10]],
                            ['十一月', res[11]],
                            ['十二月', res[12]]
                        ]
                    },
                    xAxis: {type: 'category'},
                    yAxis: {},
                    // Declare several bar series, each will be mapped
                    // to a column of dataset.source by default.
                    series: [
                        {type: 'bar'}
                    ]
                })
            }
        },
        error: function(){
            chart1.hideLoading();
        }
    })
}