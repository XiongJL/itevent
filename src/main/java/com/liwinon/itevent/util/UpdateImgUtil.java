package com.liwinon.itevent.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

public class UpdateImgUtil {
	
	public  String updateImg(List<MultipartFile> files) {
		String path = null;
        if(files.size()>0){
            for(int i=0;i<files.size();i++){
                MultipartFile mf = files.get(i);
                if(!mf.isEmpty()){
                	//取得当前上传文件的名称
	                String fileName = mf.getOriginalFilename();
	                //如果名称不为""，说明该文件存在，否则说明文件不存在。
	                if(fileName.trim()!=""){
	                	//获取文件后缀名
		                String type=fileName.substring(fileName.lastIndexOf(".")+1);
		                //获取时间戳
		                Date date = new Date();
		                SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		                String da = format.format(date);
		                //重命名上传后的文件
		                String newName = da+ getRandomString2(4) + "." + type;
		                String filePath = "D:\\ITEvent\\file\\export\\img\\";
		                fileName=filePath+newName;
		                File dest = new File(fileName);
		                // 检测是否存在目录
		                if (!dest.getParentFile().exists()) {
		                	dest.getParentFile().mkdirs();
		        		}
		                try {
		                    //上传
		                    mf.transferTo(dest);
		                    //上传后拼接路径
		                    if(i==0) {
		                    	path=newName;                    	
		                    }else {
		                    	path=","+newName;
		                    }
		                }catch (IllegalStateException e) {
		                    return "文件过大,内存溢出异常";
		                }catch (IOException e) {
		                    return "文件路径错误,IO异常";
		                }
	                }else {
	                	path=path+",图片不存在";
	                }
                }else {
                	path=path+",图片不存在";
                }
            }
        }
	    return path;	        	
	}

	public  String updateImg(MultipartFile[] files) {
		String path = null;
        if(files.length>0){
            for(int i=0;i<files.length;i++){
                MultipartFile mf = files[i];
                if(!mf.isEmpty()){
                	//取得当前上传文件的名称
	                String fileName = mf.getOriginalFilename();
	                //如果名称不为""，说明该文件存在，否则说明文件不存在。
	                if(fileName.trim()!=""){
	                	//获取文件后缀名
		                String type=fileName.substring(fileName.lastIndexOf(".")+1);
		                //获取时间戳
		                Date date = new Date();
		                SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		                String da = format.format(date);
		                //重命名上传后的文件
		                String newName = da+ getRandomString2(4) + "." + type;
		                String filePath = "D:\\ITEvent\\file\\export\\img\\";
		                fileName=filePath+newName;
		                File dest = new File(fileName);
		                // 检测是否存在目录
		                if (!dest.getParentFile().exists()) {
		                	dest.getParentFile().mkdirs();
		        		}
		                try {
		                    //上传
		                    mf.transferTo(dest);
		                    //上传后拼接路径
		                    if(i==0) {
		                    	path=newName;                    	
		                    }else {
		                    	path=","+newName;
		                    }
		                }catch (IllegalStateException e) {
		                    return "文件过大,内存溢出异常";
		                }catch (IOException e) {
		                    return "文件路径错误,IO异常";
		                }
	                }else {
	                	path=path+",图片不存在";
	                }
                }else {
                	path=path+",图片不存在";
                }
            }
        }
	    return path;	        	
	}
	
	
    public static String getRandomString2(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }

        }
        return sb.toString();
    }
}
