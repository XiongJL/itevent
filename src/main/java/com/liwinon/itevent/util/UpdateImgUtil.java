package com.liwinon.itevent.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class UpdateImgUtil {
	
	public  String updateImg(MultipartFile[] files) {
		String path = null;
		System.out.println(files.length);
        if(files.length>0){
            for(int i=0;i<files.length;i++){
                MultipartFile mf = files[i];
                String fileName = mf.getOriginalFilename();
                String filePath = "D:\\ITEvent\\file\\export\\img";
                System.out.println("文件路径:"+filePath);
                File dest = new File(filePath + fileName);
                // 检测是否存在目录
                if (!dest.getParentFile().exists()) {
                	dest.getParentFile().mkdirs();
        		}
                try {
                    //上传
                    mf.transferTo(dest);
                    //上传后拼接路径
                    if(i==0) {
                    	path=fileName;                    	
                    }else {
                    	path=path+","+fileName;
                    }
                    System.out.println(path+"======路径====================");
                }catch (IllegalStateException e) {
                    System.out.println(e);
                    return "文件过大,内存溢出异常";
                }catch (IOException e) {
                    System.out.println(e);
                    return "文件路径错误,IO异常";
                }
            }
        }
	    return path;	        	
	}
}
