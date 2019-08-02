package com.liwinon.itevent.util;

import com.liwinon.itevent.dao.primaryRepo.AdminDao;
import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.ItemDao;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.primary.Event;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//@Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
@Component
public class setExcel {
    @Autowired
    AdminDao adminDao;
    @Autowired
    SapDao sapDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    ItemDao itemDao;
    /**
     * 填入即将打印的Excel的参数值
     * @return
     */
    public String setExcel(Event event){
        String outFilePath = "";
        String eventid = event.getUuid();
        //根据事件id查询相同的其他事件
        String[] arr = eventid.split("-");
        String uuid = arr[0]+"-"+arr[1];
        Date date = event.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String eventDate = "";
        if (date==null){
            eventDate = "无记录";
        }else{
            eventDate  = sdf.format(date);
        }
        String eventType = "";
        Date create = new Date();
        String createDate =  sdf.format(create);
        String adminuser = event.getAdminuser();
        String adminName = sapDao.findNByUserId(adminDao.findByUser(adminuser).getUserid());

        String remarks = event.getRemark();
        //模糊查询相同事件
        List<Event> es =  eventDao.findLikeUuid(uuid);

        //读取模板Excel
        Workbook wb = null;
        Row row = null;
        Sheet sheet = null;
        String cellData = null;
        wb = Excel.readExcel("D:\\DOCS\\三联模板.xlsx");
        if (wb!=null){
            //设置边框style
            CellStyle style =  wb.createCellStyle();
            style.setBorderTop(BorderStyle.DOTTED);
            style.setBorderBottom(BorderStyle.DOTTED);
            style.setBorderLeft(BorderStyle.DOTTED);
            style.setBorderRight(BorderStyle.DOTTED);
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = 22;
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = 9;
            for (int i = 1; i < rownum; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        // 获取单元格数据
                        cellData = (String) Excel.getCellFormatValue(row.getCell(j));
                        if (cellData.indexOf("eventid")!=-1){
                            row.getCell(j).setCellValue("事件单号: "+uuid);
                        }
                        if (cellData.indexOf("eventdate")!=-1){
                            row.getCell(j).setCellValue("事件日期: "+eventDate);
                        }
                        if (cellData.indexOf("eventType")!=-1){
                            row.getCell(j).setCellValue("事件类型: "+eventType(event.getEvent()));
                        }
                        if (cellData.indexOf("createdate")!=-1){
                            row.getCell(j).setCellValue("制单日期: "+createDate);
                        }
                        if (cellData.indexOf("adminName")!=-1){
                            row.getCell(j).setCellValue("操作人: "+adminName);
                        }
                        if (cellData.indexOf("Remarks")!=-1){
                            row.getCell(j).setCellValue("备注: "+remarks);
                        }
                        if (cellData.indexOf("行号")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellStyle(style);
                                sheet.getRow(i+1).getCell(j).setCellValue(o);
                            }
                        }
                        if (cellData.indexOf("物料编码")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellStyle(style);
                                sheet.getRow(i+o).getCell(j).setCellValue(es.get(o-1).getItemid());
                            }
                        }
                        if (cellData.indexOf("物料类型")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellStyle(style);
                                sheet.getRow(i+o).getCell(j).setCellValue(itemDao.findByItemid(es.get(o-1).getItemid()).getType());
                            }
                        }
                        if (cellData.indexOf("物料描述")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellStyle(style);
                                sheet.getRow(i+o).getCell(j).setCellValue(itemDao.findByItemid(es.get(o-1).getItemid()).getBrand());
                            }
                        }
                        if (cellData.indexOf("单位")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellValue(itemDao.findByItemid(es.get(o-1).getItemid()).getUnit());
                            }
                        }
                        if (cellData.indexOf("数量")!=-1){
                            for (int o = 1; o<=es.size();o++){
                                sheet.getRow(i+o).getCell(j).setCellStyle(style);
                                sheet.getRow(i+o).getCell(j).setCellValue(es.get(o-1).getCount());
                            }
                        }
                    }
                } else {
                    System.out.println("获取row为空!");
                    continue;
                }
            }
            FileOutputStream out = null;
            try {
                outFilePath= "D:\\DOCS\\ITEventFile\\"+uuid+".xlsx";
                out = new FileOutputStream(outFilePath);
                wb.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return outFilePath;
        }
        return null;
    }

    public String eventType(int event){
        if (event==1){
            return "以旧换新";
        }else if (event==2){
            return "报废";
        }else if (event==3){
            return "新资产入库";
        }else if (event==4){
            return "退还";
        }else if (event==5){
            return "借用";
        }else if (event==6){
            return "借用出库";
        }else if (event==7){
            return "提单领取";
        }else if (event==8){
            return "已有资产登记";
        }else if (event==9){
            return "物料登记";
        }

        return null;

    }
}
