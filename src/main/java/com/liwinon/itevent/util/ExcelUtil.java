package com.liwinon.itevent.util;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 读取Excel的工具
 *
 * @author XiongJL
 * @docUrl https://poi.apache.org/apidocs/4.1/ HSSF是POI工程对Excel97 (.xls)
 *         文件操作的纯Java实现 XSSF是POI工程对Excel 2007 OOXML (.xlsx)文件操作的纯Java实现
 *         SXSSF通过一个滑动窗口来限制访问Row的数量从而达到低内存占用的目录
 */
public class ExcelUtil {
    // 构造函数
    public ExcelUtil(String path) {

    }

    //读取Excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null || filePath == "") {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wb;
    }
    //获取单元格的数据
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case NUMERIC:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellValue = sdf.format(cell.getDateCellValue());
                    }else{
                        //数字
                        String value =String.valueOf(cell.getNumericCellValue());
                        if (value.indexOf(".")!=-1){
                            cellValue = value.split("\\.")[0];
                        }else {
                            cellValue = value;
                        }

                    }
                    break;
                }
                case FORMULA:{
                    //判断cell是否为公式格式
                    cellValue = cell.getStringCellValue();
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    //测试
                    if (cell.getRichStringCellValue().getString()=="1"){
                        System.out.println(1);
                    }
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    //写入Excel,返回文件路径
    //filePath    ..../file.xlsx
    public static void writeExcel(String filePath, List<String[]> data) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("数据页");
        for(int i = 0;i<data.size();i++){ //循环行
            Row row = sheet.createRow(i); //创建行,从0开始
            for(int j=0;j<data.get(i).length;j++){  //循环每行多少列
                Cell cell = row.createCell(j);
                if(data.get(i)[j]!=null){
                    cell.setCellValue(data.get(i)[j]); //设置单元格内容
                }

            }
        }
        FileOutputStream out = new FileOutputStream(filePath);
        workbook.write(out);
        out.close();
    }
}
