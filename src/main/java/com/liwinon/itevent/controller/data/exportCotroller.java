package com.liwinon.itevent.controller.data;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.util.Excel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/itevent")
public class exportCotroller {
    @Autowired
    AssetsDao assetsDao;

    @GetMapping(value = "/download/using")
    @PasssToken
    public ResponseEntity<InputStreamResource> exportITExcel() {
        List<String[]> data = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
        String filepath = "D:\\ITEvent\\file\\export\\IT资产文档" + sdf.format(new Date()) + ".xlsx";
        //添加第一行
        data.add(new String[]{"ID","资产牌","使用人","工号","联系方式","物理位置",
                "物料编码","物料名称","物料描述","状态","所在仓","购买日期"});
        List<String[]> values = assetsDao.export();
        for (String[] strs : values) {
            data.add(new String[]{strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6],
                    strs[7], strs[8], strs[9], strs[10], strs[11]});
        }
        try {
            Excel.writeExcel(filepath, data);
        } catch (Exception e) {
            System.out.println("导出失败!");
            e.printStackTrace();
        }
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
}
