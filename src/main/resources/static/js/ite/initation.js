var token = localStorage.getItem("token");
//JavaScript代码区域
var element,form,layer = null
$.ajax({
	async : false,
	type : "GET",
	url : "/itevent/initiation/level_1",
	dataType : "json",
	data : {},
	success : function(res) {
		data=res.data;
		var option = "";
		for(var i = 0; i <data.length; i++) {
			option += '<option value="'+data[i]+'">' + data[i] + '</option>';
		}
		select2=data[0];
		$("#level_1").append(option);
		chushihua(select2);
	}
});	
function chushihua(aaa){
	var bb;
	 $.ajax({
			async : false,
			type : "GET",
			url : "/itevent/initiation/level_2",
			dataType : "json",
			data : {value:aaa},
			success : function(res) {
				$("#level_2").empty(); 
				data=res.data;
				var option = "";
				bb=data[0];
				for(var i = 0; i <data.length; i++) {
					option += '<option value="'+data[i]+'">' + data[i] + '</option>';
				}
				$("#level_2").append(option);
				form.render('select');
			}
		});	
	 $.ajax({
			async : false,
			type : "GET",
			url : "/itevent/initiation/description",
			dataType : "json",
			data : {value:bb},
			success : function(res) {
				$("#description").val(""); 
				data=res.data;
				$("#description").val(data);
			}
		});	
}
layui.use(['form','layer','element','laydate'], function(){
    element = layui.element,
        form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate;
    //日期选择器初始化
    laydate.render({ 
    	  elem: '#stepdate'
    	  ,type: 'datetime'
    	});
    laydate.render({ 
    	elem: '#date'
    		,type: 'datetime'
    });
    form.on('select(filter1)', function(data){
		// alert(data.value); //得到被选中的值
		 var aaa=data.value;
		 var bb;
		 $.ajax({
				async : false,
				type : "GET",
				url : "/itevent/initiation/level_2",
				dataType : "json",
				data : {value:aaa},
				success : function(res) {
					$("#level_2").empty(); 
					data=res.data;
					for(var i = 0; i <data.length; i++) {
						var option = "";
						if(i==0){
							bb=data[i];							
						}
						option = '<option value="'+data[i]+'">' + data[i] + '</option>';
						$("#level_2").append(option);
						 form.render('select');
					}
				}
			});	
		 $.ajax({
				async : false,
				type : "GET",
				url : "/itevent/initiation/description",
				dataType : "json",
				data : {value:bb},
				success : function(res) {
					$("#description").val(""); 
					data=res.data;
					$("#description").val(data);
				}
			});	
		}); 
    form.on('select(filter2)', function(data){
		// alert(data.value); //得到被选中的值
    	$("#description").val(""); 
		 var aaa=data.value;
		 $.ajax({
				async : false,
				type : "GET",
				url : "/itevent/initiation/description",
				dataType : "json",
				data : {value:aaa},
				success : function(res) {
					data=res.data;
					$("#description").val(data);
				}
			});	
		}); 
    //通过layui验证输入后提交 ,需要将提交按钮放入form内
    form.on('submit(sub)', function (data) {
        console.log($(data.form).serialize())
        var data = $('#initation').serialize();
        console.log(typeof data)
        //告诉后台有多少物料
        console.log(data)
        $.ajax({
            url: "/itevent/initiation/postinitiation",
            data: data,
            type: "post",
            beforeSend:function(XMLHttpRequest){
                XMLHttpRequest.setRequestHeader("token",token);
            },
            success: function (res) {
                console.log(res)
               if (res.data=="ok"){
                    layer.msg("操作成功",{icon: 1})
                }else{
                    layer.msg(res.msg)
                }
            }
        });
        return false;
    });

});
$(function () {
    //添加导航栏选中样式
    $("#back").addClass("layui-this");
    $("#eventManager-nav").removeClass("layui-nav-itemed");
    $("#assets-nav").addClass("layui-nav-itemed");
    //赋值初始化数值
})
//自动查询责任人工号
function selectaa(aaa){
	 $.ajax({
			async : false,
			type : "GET",
			url : "/itevent/initiation/level_2",
			dataType : "json",
			data : {value:aaa},
			success : function(res) {
				$("#level_2").empty(); 
				data=res.data;
				for(var i = 0; i <data.length; i++) {
					var option = "";
					var optiona = "";
					option = '<option value="'+data[i][0]+'">' + data[i][0] + '</option>';
					optiona = '<option value="'+data[i][1]+'">' + data[i][1] + '</option>';
					$("#level_2").append(option);
					$("#description").append(optiona);
					 form.render('select');
				}
			}
		});	
}