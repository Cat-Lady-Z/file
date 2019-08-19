package com.util.file.util.excel.excel;

import com.util.file.common.ManageResultCode;
import com.util.file.util.ManageException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @ClassName: 导出表格
 * @Description:
 */
public class ExportExcelUtil {
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param content 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, List<String> title, List<List<String>> content, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(title.get(i));
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < content.size(); i++) {
            row = sheet.createRow(i + 1);
            for(int j=0;j<content.get(i).size();j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(content.get(i).get(j));
            }
        }

        return wb;
    }

    /**
     * 导出excel到网页
     * @param fileName
     * @param response
     * @param wb
     */
    public static void exportExcel(String fileName, HttpServletResponse response, HSSFWorkbook wb, HttpServletRequest httpServletRequest) {
        ServletOutputStream outputStream = null;
        try {
            //设置响应头
            fileName = fileName + ".xls";
            setResponseHeader(response, fileName, httpServletRequest);

            //将文件读入响应流
            outputStream = response.getOutputStream();
            wb.write(outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setResponseHeader(HttpServletResponse response, String fileName, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String userAgent = httpServletRequest.getHeader("USER-AGENT");
        System.out.println("USER-AGENT:" + userAgent);
        response.reset();
        response.setContentType("application/octet-stream;charset=utf-8");

        try {
            if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
                    || userAgent.indexOf("Safari") >= 0) {
                fileName= new String((fileName).getBytes(), "ISO8859-1");
            } else {
                fileName=URLEncoder.encode(fileName,"UTF8"); //其他浏览器

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //attachment作为附件下载；inline客户端机器有安装匹配程序，则直接打开；注意改变配置，清除缓存，否则可能不能看到效果
        response.setHeader("Content-Disposition",   "attachment;filename="+ fileName);
    }

    /**
     * 下载excel表格（调试）
     * @param file
     * @param fileName
     * @param response
     */
    public static void download(File file, String fileName, HttpServletResponse response, HttpServletRequest httpServletRequest) {
        // 下载文件
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        if(!file.exists()){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "文件不存在!");
        }

        try {
            //设置响应头
            setResponseHeader(response,fileName, httpServletRequest);

            //将文件读入响应流
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int length = 1024;
            int readLength=0;
            byte buf[] = new byte[1024];
            readLength = inputStream.read(buf, 0, length);
            while (readLength != -1) {
                outputStream.write(buf, 0, readLength);
                readLength = inputStream.read(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载excel表格(jar包)
     * @param stream
     * @param fileName
     * @param response
     */
    public static void downloadInstream(InputStream stream, String fileName, HttpServletResponse response, HttpServletRequest httpServletRequest) {
        // 下载文件
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        if(stream == null){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "文件不存在!");
        }

        try {
            //设置响应头
            setResponseHeader(response,fileName, httpServletRequest);

            //将文件读入响应流
            inputStream = stream;
            outputStream = response.getOutputStream();
            int length = 1024;
            int readLength=0;
            byte buf[] = new byte[1024];
            readLength = inputStream.read(buf, 0, length);
            while (readLength != -1) {
                outputStream.write(buf, 0, readLength);
                readLength = inputStream.read(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
