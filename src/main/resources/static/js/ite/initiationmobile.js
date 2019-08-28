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
$.ajax({
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
    form.on('select(type1)', function(data){
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
    });
});
//赋值初始化数值
var files=[];
var filesa=[];
var result;
var aa;
var dataArr = []; // 储存所选图片的结果(文件名和base64数据)
var fd;
var that = this;
var fileArr=new Array();
$(function () {
    //添加导航栏选中样式
    $("#back").addClass("layui-this");
    $("#eventManager-nav").removeClass("layui-nav-itemed");
    $("#assets-nav").addClass("layui-nav-itemed");

    var tmpl = '<li class="weui-uploader__file" style="background-image:url(#url#)"></li>',  
    $gallery = $("#gallery"),  
    $galleryImg = $("#galleryImg"),  
    $uploaderInput = $("#uploaderInput"),  
    $uploaderFiles = $("#uploaderFiles");  
	$uploaderInput.on("change", function(e) {  
	    var src, url = window.URL || window.webkitURL || window.mozURL,  
	    files = e.target.files;
	    var length=fileArr.length;
	    for(var i = 0, len = files.length; i < len; ++i) {  
	        var file = files[i];
	        if(length + i + 1 > 5) {
	            break;
	        }  
				fileArr.push(file);
	        if(url) {  
	            src = url.createObjectURL(file);  
	        } else {  
	            src = e.target.result;  
	        }  
	        $uploaderFiles.append($(tmpl.replace('#url#', src)));  
	    }  
	    checkPhotoSize();
	}); 
	
	//控制显示三张以内照片
	function checkPhotoSize(){
	    if(fileArr.length>5){
	    	$(".weui-uploader__input-box").hide();
	    }else{
	    	$(".weui-uploader__input-box").show();
	    }
	}
	     
	var index; //第几张图片  
	$uploaderFiles.on("click", "li", function() {  
	    index = $(this).index();  
	    $galleryImg.attr("style", this.getAttribute("style"));  
	    $gallery.fadeIn(100);  
	});  
	$gallery.on("click", function() {  
	    $gallery.fadeOut(100);  
	});  
	//删除图片  
	$(".weui-gallery__del").click(function() {  
	    $uploaderFiles.find("li").eq(index).remove();
	    fileArr.splice(index,index);
	    checkPhotoSize();  
	});
	
		var adminuser=$("#adminuser").val();
		if(adminuser==""||adminuser==null||adminuser==undefined){//web端 上传
	
		}else{ //移动端上传
	
		}
})
$("#submit").click(function() {
            if(!fileArr.length){
            	layer.msg("请选择要上传的图片", {
        			  icon: 2,
          			  time: 2000});
                return false;
            }
          		var formData = new FormData();
          		for(var i = 0;i<fileArr.length;i++){
          			formData.append("file",fileArr[i]);
          		}
          		 var userid = document.getElementById("userid").value;
                 if(userid==""||userid==null||userid==undefined){
                 	layer.msg("请填写发起人工号", {
           			  icon: 2,
           			  time: 2000});
                 	return false;
                 }else{
                	 $.ajax({
                		async : false,
         		        url:"/itevent/api/getNamePersonid",
         		        data: {userid:userid},
         		        beforeSend:function(XMLHttpRequest){
         		            XMLHttpRequest.setRequestHeader("token",token);
         		        },
         		        success:function (data) {
         		        	if(data==""||data==null||data==undefined){
         		        		layer.alert("请填写正确的工号", {
                       			  icon: 2
                       			 });
         		        	}
         		        }
         		    })
                 }
                 var phone = document.getElementById("phone").value;
                 if(phone==""||phone==null||phone==undefined){
                 	layer.msg("请填写发起人电话", {
           			  icon: 2,
           			  time: 2000});
                 	return false;
                 }
                 formData.append("userid",userid);
                 formData.append("phone",phone);
                 var wxuserid = document.getElementById("wxuserid").value;
                 formData.append("wxuserid",wxuserid);
                 var adminuser = document.getElementById("adminuser").value;
                 formData.append("adminuser",adminuser);
                 var level_1 = document.getElementById("level_1").value;
                 formData.append("level_1",level_1);
                 var level_2 = document.getElementById("level_2").value;
                 formData.append("level_2",level_2);
                 var description = document.getElementById("description").value;
                 formData.append("description",description);
                 var type = document.getElementById("type").value;
                 formData.append("type",type);
                 var brand = document.getElementById("brand").value;
                 formData.append("brand",brand);
                 var itemid = document.getElementById("itemid").value;
                 formData.append("itemid",itemid);
                 var remark = document.getElementById("remark").value;
                 formData.append("remark",remark);
                 $.ajax({
                     url : '/itevent/initiation/initiationmobile',
                     type : 'post',
                     data : formData,
                     async: false,  
					 cache: false,
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