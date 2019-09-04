var token = localStorage.getItem("token");
//JavaScript代码区域
var element,
form,
layer = null
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
/*$.ajax({
	async : false,
	type : "GET",
	url : "/itevent/api/getTypes",   //物料名称
	dataType : "json",
	data : {},
	 beforeSend:function(XMLHttpRequest){
         XMLHttpRequest.setRequestHeader("token",token);
     },
	success : function(res) {
		data=res;
		var option = "";
		for(var i = 0; i <data.length; i++) {
			option += '<option value="'+data[i]+'">' + data[i] + '</option>';
		}
		select2=data[0];
		$("#type").append(option);
		chushihuab(select2);
	}
});
function chushihuab(select2){
	var bb;
	$.ajax({
		async : false,
		type : "GET",
		url : "/itevent/api/getBrands",
		dataType : "json",
		 beforeSend:function(XMLHttpRequest){
	            XMLHttpRequest.setRequestHeader("token",token);
	        },
		data : {type:select2},
		success : function(res) {
			$("#brand").empty(); 
			data=res;
			var option = "";
			bb=data[0];
			for(var i = 0; i <data.length; i++) {
				option += '<option value="'+data[i]+'">' + data[i] + '</option>';
			}
			$("#brand").append(option);
		}
	});	
	$("#itemid").empty();
    $.ajax({
        url:"/itevent/api/getItemId",
        data: {type:select2,brand:bb},
        beforeSend:function(XMLHttpRequest){
            XMLHttpRequest.setRequestHeader("token",token);
        },
        success:function (res) {
            if (res!=null && res!=""){
                $("#itemid").val(res)
            }
        }
    })
}*/
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
   /* form.on('select(type1)', function(data){
		// alert(data.value); //得到被选中的值
    	var bb;
		 var aaa=data.value;
		 $.ajax({
				async : false,
				type : "GET",
				url : "/itevent/api/getBrands",
				dataType : "json",
				 beforeSend:function(XMLHttpRequest){
			            XMLHttpRequest.setRequestHeader("token",token);
			        },
				data : {type:aaa},
				success : function(res) {
					$("#brand").empty(); 
					data=res;
					var option = "";
					bb=data[0];
					for(var i = 0; i <data.length; i++) {
						option += '<option value="'+data[i]+'">' + data[i] + '</option>';
					}
					$("#brand").append(option);
					form.render('select');
				}
			});	
		 $("#itemid").empty();
		    $.ajax({
		        url:"/itevent/api/getItemId",
		        data: {type:aaa,brand:bb},
		        beforeSend:function(XMLHttpRequest){
		            XMLHttpRequest.setRequestHeader("token",token);
		        },
		        success:function (res) {
		            if (res!=null && res!=""){
		                $("#itemid").val(res)
		            }
		        }
		    })
		 
		});
    form.on('select(brand1)', function(data){
	    $.ajax({
	        url:"/itevent/api/getItemId",
	        data: {type:$("#type").val(),brand:data.value},
	        beforeSend:function(XMLHttpRequest){
	            XMLHttpRequest.setRequestHeader("token",token);
	        },
	        success:function (res) {
	            if (res!=null && res!=""){
	                $("#itemid").val(res)
	            }
	        }
	    })
    });*/
});
//赋值初始化数值
var files=[];
var filesa=[];
var result;
var aa;
var dataArr = []; // 储存所选图片的结果(文件名和base64数据)
var fd;
var that = this;
$(function () {
    //添加导航栏选中样式
    $("#back").addClass("layui-this");
    $("#eventManager-nav").removeClass("layui-nav-itemed");
    $("#assets-nav").addClass("layui-nav-itemed");

        var uploadBtn = document.querySelector('#upload');
        var previewImgList = document.querySelector('.preview_img_list');

        imgArr = new Array();
        uploadBtn.addEventListener('change',function(){
        	fd = null ;
            fd = new FormData();
            var iLen = this.files.length;
            if(aa>5){
            	layer.alert("最多上传5张图片", {
      			  icon: 1,
      			  time: 1500});
            	return false;
            }
            for(var i=0;i<iLen;i++){
            	aa++;
                var reader = new FileReader();
                var aaaa=this.files[i].name;
                console.log(this.files[i])
                var ab="ok";
                fd.append("file",this.files[i]);
                reader.readAsDataURL(this.files[i]);  //转成base64
                reader.fileName = this.files[i].name;
                filesa.push(this.files[i].name);
                reader.onload = function(e){
                    var imgMsg = {
                        name : reader.fileName,//获取文件名
                        base64 : reader.result   //reader.readAsDataURL方法执行完后，base64数据储存在reader.result里
                    }
                    dataArr.push(imgMsg);
                    result = '<div class="layui-col-xs12 layui-col-md4" style="margin-right: 10px;float: left;"><img class="subPic" src="'+this.result+'" alt="'+this.fileName+'"/><div class="delete tc" color="red">删除</div></div>';
                    var div = document.createElement('div');
                    div.innerHTML = result;
                    div['className'] = 'float';
                    document.getElementsByClassName("gallery")[0].appendChild(div);  　　//插入dom树
                    var img = div.getElementsByTagName('img')[0];
                    img.onload = function(){
                        var nowHeight = ReSizePic(this); //设置图片大小
                        this.parentNode.style.display = 'block';
                        var oParent = this.parentNode;
                        if(nowHeight){
                            oParent.style.paddingTop = (oParent.offsetHeight - nowHeight)/2 + 'px';
                        }
                    }
                    div.onclick = function(){
                        $(this).remove();                  // 在页面中删除该图片元素
                    }
                }
            }
        },false);
        

	// 初始化图片宽度
	// 使得图片高度一致
	function ReSizePic(ThisPic) {
		var RePicWidth = 200; //这里修改为您想显示的宽度值

		var TrueWidth = ThisPic.width; //图片实际宽度
		var TrueHeight = ThisPic.height; //图片实际高度

		if(TrueWidth>TrueHeight){
			//宽大于高
			var reWidth = RePicWidth;
			ThisPic.width = reWidth;
			//垂直居中
			var nowHeight = TrueHeight * (reWidth/TrueWidth);
			return nowHeight;  //将图片修改后的高度返回，供垂直居中用
		}else{
			//宽小于高
			var reHeight = RePicWidth;
			ThisPic.height = reHeight;
		}
	}

	var adminuser=$("#adminuser").val();
	if(adminuser==""||adminuser==null||adminuser==undefined){//web端 上传

	}else{ //移动端上传

	}
})
function gonghaoid(){
	var da;
	var y=$("#userid").val();
		$.ajax({
				async : false,
				type : "GET",
				url : "/itevent/api/getNamePersonid",
				data : {userid:y},
				 beforeSend:function(XMLHttpRequest){
	                   	XMLHttpRequest.setRequestHeader("token",token);
	                   },
				success : function(res) {
		        	da=res.data;
				}
			});
		if(da==null||da==undefined||da==""){
    		layer.msg("请填写正确的工号", {
			  icon: 2,
  			  time: 1500
			 });
    		$("#userid").val("");
    	}
}
$("#submit").click(function() {
          /*  if(!filesa.length){
            	layer.msg("请选择要上传的图片", {
        			  icon: 2,
          			  time: 1500});
                return false;
            }*/
            var phone = document.getElementById("phone").value;
            if(phone==""||phone==null||phone==undefined){
            	layer.msg("请填写发起人电话", {
    			  icon: 2,
    			  time: 1500});
            	return false;
            }
            var userid = document.getElementById("userid").value;
            if(userid==""||userid==null||userid==undefined){
            	layer.msg("请填写发起人工号", {
      			  icon: 2,
      			  time: 1500});
            	return false;
            }else{
  		               fd.append("userid",userid);
  		               fd.append("phone",phone);
  		               var adminuser = document.getElementById("adminuser").value;
  		               fd.append("adminuser",adminuser);
  		               var level_1 = document.getElementById("level_1").value;
  		               fd.append("level_1",level_1);
  		               var level_2 = document.getElementById("level_2").value;
  		               fd.append("level_2",level_2);
  		               var description = document.getElementById("description").value;
  		               fd.append("description",description);
  		              /* var type = document.getElementById("type").value;
  		               fd.append("type",type);
  		               var brand = document.getElementById("brand").value;
  		               fd.append("brand",brand);
  		               var itemid = document.getElementById("itemid").value;
  		               fd.append("itemid",itemid);*/
  		               var location = document.getElementById("location").value;
  		               fd.append("location",location);
  		               var remark = document.getElementById("remark").value;
  		               fd.append("remark",remark);
  		               $.ajax({
  		                   url : '/itevent/initiation/initiationpc',
  		                   type : 'post',
  		                   data : fd,
  		                   processData: false,   //用FormData传fd时需有这两项
  		                   contentType: false,
  		                   timeout:600000,
  		                   beforeSend:function(XMLHttpRequest){
  		                   	XMLHttpRequest.setRequestHeader("token",token);
  		                   },
  		                   success : function(res){
  		                   	if(res.data=="ok"){
  		                   		layer.msg(res.msg, {
  		                 			  icon: 1
  		                 			 });
  		                   		setTimeout( function(){
                     			//1.5秒后实现的方法写在这个方法里面     延时刷新
                     			location.reload();
                     			}, 1500 );
  		                   	}else if(res.data=="no1"){
  		                   		layer.alert(res.msg, {
  		                   			  icon: 2
  		                   			 });
  		                   	}
  		                   },
  		                   error: function (err) {
  		                   	layer.alert('数据上传失败', {
  		             			  icon: 2
  		             			 });
  		                   }
  		               });
  		        	}
 });
/*
document.getElementById("gallery").innerHTML="";
var img=this.files; 
var div=document.createElement("div");
for(var i=0;i<img.length;i++){
    var file=img[i]; var url=URL.createObjectURL(file); 
    var box=document.createElement("img"); 
    box.setAttribute("src",url); 
    box.className='img';

    var imgBox=document.createElement("div");
    imgBox.style.display='inline-block';
    imgBox.className='img-item';

    imgBox.appendChild(box);
    var body=document.getElementsByClassName("gallery")[0]; 
    body.appendChild(imgBox);
    
    var deleteIcon = document.createElement("span");
    deleteIcon.className = 'delete';
    deleteIcon.innerText = '删除';
    deleteIcon.dataset.filename = img[i].name;
    imgBox.appendChild(deleteIcon);
    
    that.files = img;
    $(deleteIcon).click(function () {
        var filename = $(this).data("filename");
        $(this).parent().remove();
        var fileList = Array.from(that.files);

        for(var j=0;j<fileList.length;j++){
            if(fileList[j].name = filename){
                fileList.splice(j,1);
                break;
            }
        }
        that.files = fileList
    })
}*/