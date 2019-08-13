package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.EventService;
import com.liwinon.itevent.util.ExcelUtil;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/itevent")
public class oldAssetsController {

    @Autowired
    EventService eventService;
    @GetMapping(value = "/oldAssets")
    @PasssToken
    public String oldAssets(){
        return "event/oldAssets";
    }

    @PostMapping(value = "/oldAssets")
    @ResponseBody
    public String oldAssetsEvent(int index, int event, HttpServletRequest request){
        return eventService.oldAssetsEvent(index,event,request);
    }
    
    
    /**
     * 导出IT资产数据模板
     * @return
     */
    @GetMapping(value = "/oldAssets/dowexcel")
    @PasssToken
    public ResponseEntity<InputStreamResource> exportITExcel() {
        List<String[]> data = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
        String filepath = "D:\\ITEvent\\file\\export\\IT事件管理" + sdf.format(new Date()) + ".xlsx";
        System.out.println(filepath);
        String filePatha = "D:\\ITEvent\\file\\export\\"; //D:\\ITEVENT\\file\\export\\IT事件管理" + sdf.format(new Date()) + ".xlsx
        File dir = new File(filepath); 
        if(dir.exists()){    //查看路径是否存在。不存在就创建
            dir.mkdirs();   //mkdirs可以在不知道偶没有父类文件夹的情况下，创建文件夹，而mkdir（）必须在有父类的文件夹下创建文件
            System.out.println(11);
        }
        data.add(new String[]{"ID", "资产牌", "使用人", "工号", "联系方式", "物理位置", "物料编码",
                "物料名称", "物料描述", "状态", "所在仓","备注"}); //第一行   资产状态(0在库/1不在库/2不在库借用/3废品)
        data.add(new String[]{"12342", "xx-x-xxxxx", "张三", "xxxxxxxx", "1xxxxxxxxxx", "4栋1楼", "xxxx",
                "打印机", "惠普打印机xx型号", "在库/不在库/不在库借用/废品", "资产仓/费用仓", "轻放缓拿"});//示例数据     
        try {
            ExcelUtil.writeExcel(filepath, data);
        } catch (Exception e) {
            System.out.println("导出失败!");
            e.printStackTrace();
        }
        // 此处可以添加其他示例文档
        FileSystemResource file = new FileSystemResource(filepath);
        try {
            String fileName = new String(file.getFilename().getBytes(), "iso8859-1");
            if (file.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
                headers.add("Content-Disposition", "attachment;fileName=" + fileName);
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                return ResponseEntity.ok().headers(headers).contentLength(file.contentLength())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(new InputStreamResource(file.getInputStream()));
            }
        } catch (Exception e) {
            System.out.println("下载异常!");
            e.printStackTrace();
        }

        return null;
    }
    
    
    
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/oldAssets/excel")
    @PasssToken
    @ResponseBody
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        System.out.println("开始上传文件");
        JSONObject json = new JSONObject();
        if (file.isEmpty()) {
        	   json.accumulate("code",400);
               json.accumulate("msg","ok");
               json.accumulate("count",400);
            return json;
        }
        String fileName = file.getOriginalFilename();
        if(fileName.indexOf("\\")!=-1){  //浏览器设置为显示真实路径 ,则需要处理
            String[] strs = fileName.split("\\\\");
            fileName = strs[strs.length-1];
        }
        System.out.println("文件名:"+fileName);
        String filePath = "D:\\ITEVENT\\file\\export\\"; //D:\\ITEVENT\\file\\export\\IT事件管理" + sdf.format(new Date()) + ".xlsx
        File tempFile = new File(filePath + fileName);
        if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
        try {
            file.transferTo(tempFile);
            System.out.println(tempFile.getPath());
            System.out.println(filePath + fileName);
            //保存操作信息
            json.accumulate("code",0);
            json.accumulate("msg","ok");
            json.accumulate("count",400);
            json.accumulate("data",filePath + fileName);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        json.accumulate("code",400);
        json.accumulate("msg","ok");
        json.accumulate("count",400);
        return json;
    }
    
    /**
     * 解析上传的excel文件
     * @param path
     * @param type 可以选择解析的文件类型,
     * @return
     */
    @GetMapping(value = "/oldAssets/resolvExcel")
    @PasssToken
    @ResponseBody
    public JSONObject resolve(String path) {
    	JSONObject json= new JSONObject();
        System.out.println("开始解析");
        /*String[] columns = new String[]{"ID", "资产牌", "使用人", "工号", "联系方式", "物理位置", "物料编码",
                "物料名称", "物料描述", "状态", "所在仓", "备注"};*/
        return eventService.resolveExcel(path);
    }
}
