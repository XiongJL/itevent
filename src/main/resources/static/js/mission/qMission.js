var token = localStorage.getItem("token");

layui.use(['element', 'layer','carousel'], function(){
    var element = layui.element;
    var layer = layui.layer;

    //监听折叠
    element.on('collapse(test)', function(data){
        // layer.msg('展开状态：'+ data.show);
    });
    layer.photos({
        photos: '#photos'
        ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
    });
//触发事件
    var active = {
        notice: function(){
            //示范一个公告层
            layer.open({
                type: 1
                ,title: false //不显示标题栏
                ,closeBtn: false
                ,area: '300px;'
                ,shade: 0.8
                ,id: 'choose' //设定一个id，防止重复弹出
                ,btn: ['火速围观', '残忍拒绝']
                ,btnAlign: 'c'
                ,moveType: 1 //拖拽模式，0或者1
                ,content: ''
                ,success: function(layero){
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').attr({
                        href: 'http://www.layui.com/'
                        ,target: '_blank'
                    });
                }
            });
        }

    };
    $('#move').on('click', function(){
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });
    var active = {
        notice: function(){
            $.ajax({
                url:"/itevent/mission/CanTurnOverUsers",
                data:{"uuid":uuid,"qyid":qyid},
                beforeSend:function(XMLHttpRequest){
                    XMLHttpRequest.setRequestHeader("token",token);
                },
                success:function (res) {
                    console.log(res);
                    //示范一个公告层
                    layer.open({
                        type: 1
                        ,title: false //不显示标题栏
                        ,closeBtn: false
                        ,area: '300px;'
                        ,shade: 0.8
                        ,id: 'choose' //设定一个id，防止重复弹出
                        ,btn: ['火速围观', '残忍拒绝']
                        ,btnAlign: 'c'
                        ,moveType: 1 //拖拽模式，0或者1
                        ,content: ''
                        ,success: function(layero){
                            var btn = layero.find('.layui-layer-btn');
                            btn.find('.layui-layer-btn0').attr({
                                href: 'http://www.layui.com/'
                                ,target: '_blank'
                            });
                        }
                    });
                }
            })
        }
    }


});