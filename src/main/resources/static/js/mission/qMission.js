var token = localStorage.getItem("token");
var toUser = "";
layui.use(['form','element', 'layer','carousel'], function(){
    var element = layui.element
        ,layer = layui.layer
        ,form = layui.form;

    //监听折叠
    element.on('collapse(test)', function(data){
        // layer.msg('展开状态：'+ data.show);
    });
    layer.photos({
        photos: '#photos'
        ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
    });
    //点击移交
    $('#move').on('click', function(){
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });
    //监听下拉框
    form.on('select(toUser)', function(data){
        console.log(data.value); //得到被选中的值
        toUser = data.value;
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
                    console.log(personid)
                    console.log(res);
                    if (res.code=="exception"){
                        //后台有值为null
                        layer.msg("您无操作权限.",{icon: 5})
                    }
                    else if (res.code=="notMatch"){
                        layer.msg(res.data,{icon: 5})
                    }
                    else if (res.code=="ok"){
                        var option = "";
                        for (var x in res.data){
                            option += "<option value='"+res.data[x].personid+"'>"+res.data[x].name+"</option>"
                        }
                        //公告层
                        setTimeout(function () {
                            layer.open({
                                type: 1
                                ,title: false //不显示标题栏
                                ,closeBtn: false
                                ,area: ['80%','80%']
                                ,shade: 0.8
                                ,id: 'choose' //设定一个id，防止重复弹出
                                ,btn: ['确定', '取消']
                                ,btnAlign: 'c'
                                ,moveType: 1 //拖拽模式，0或者1
                                ,content: '<h2 class="" align="center">选择移交人</h2>' +
                                    '<form class="layui-form">\n' +
                                    '            <div class="">\n' +
                                    '                <div class="layui-input-inline" style="width: 85%;margin: 22px">\n' +
                                    '                    <select name="interest" lay-filter="toUser">\n' +
                                    option+
                                    '                    </select>\n' +
                                    '                </div>\n' +
                                    '            </div>\n' +
                                    '        </form>'
                                ,success: function(layero){
                                    form.render('select');
                                    //console.log(layero)
                                }
                                ,yes:function (index,layero) { //按钮1 回调是yes ,后续都是 btn2 btn3....
                                    if (toUser=="" ||toUser==qyid || toUser==personid){
                                        layer.msg("不能选择自己")
                                    }else{
                                        //请求移交人员
                                        $.ajax({
                                            url:"/itevent/mission/changeUser",
                                            data:{"toPersonid":toUser,"fromPersonid":personid,"uuid":uuid
                                            ,"qyid":qyid},
                                            beforeSend:function(XMLHttpRequest){
                                                XMLHttpRequest.setRequestHeader("token",token);
                                            },
                                            success:function (res) {
                                                if (res.code =="ok"){

                                                    layer.msg("移交成功,即将刷新!");
                                                    setTimeout(function () {
                                                        location.reload();
                                                    },2000)
                                                }else{
                                                    layer.msg(res.msg,{icon:5});
                                                }
                                                //关闭弹出层
                                                layer.close(index)
                                            },
                                            error : function (err) {
                                                console.log(err)
                                            }
                                        })

                                    }
                                }
                            });
                        },100)
                    }

                }
            })
        }
    }
    
    $('#done').on('click', function(){
	    //请求完成
	    $.ajax({
	        url:"/itevent/mission/complete",
	        data:{"fromPersonid":personid,"uuid":uuid
	        ,"qyid":qyid},
	        beforeSend:function(XMLHttpRequest){
	            XMLHttpRequest.setRequestHeader("token",token);
	        },
	        success:function (res) {
	        	if (res.code =="ok"){
	                layer.msg(res.msg);
	                setTimeout(function () {
	                },2000)
	            }else{
	                layer.msg(res.msg,{icon:5});
	            }
	            //关闭弹出层
	            layer.close(index)
	        },
	        error : function (err) {
	            console.log(err)
	        }
	    })
    });

});