package com.util.file.controller;

import com.util.file.common.ShopExportExcelTitle;
import com.util.file.service.DataImportService;
import com.util.file.util.DateUtil;
import com.util.file.util.excel.excel.ExportExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入
 */
@RestController
@RequestMapping(value = "/import")
public class SQLDataImportController {

    @Autowired
    private DataImportService dataImportService;

    @RequestMapping(value="/importCSVData",method= RequestMethod.GET)
    public void importCSVData(HttpServletRequest request, HttpServletResponse response){
        //dataImportService.importCSVData();
       // String filePath1 ="E://O2O_data//beele_01.csv";
        String filePath2 ="E://O2O_data//beele_02.csv";
        String filePath3 ="E://O2O_data//beele_03.csv";
        String filePath4 ="E://O2O_data//beele_04.csv";
        String filePath5 ="E://O2O_data//beele_05.csv";
        String filePath6 ="E://O2O_data//beele_06.csv";
        String filePath7 ="E://O2O_data//beele_07.csv";
        //List<List<String>> importCSVData1 = dataImportService.importCSVData1(filePath1);
        List<List<String>> importCSVData2 = dataImportService.importCSVData2(filePath2);
        List<List<String>> importCSVData3 = dataImportService.importCSVData3(filePath3);
        List<List<String>> importCSVData4 = dataImportService.importCSVData4(filePath4);
        List<List<String>> importCSVData5 = dataImportService.importCSVData5(filePath5);
        List<List<String>> importCSVData6 = dataImportService.importCSVData1(filePath6);
        List<List<String>> importCSVData7 = dataImportService.importCSVData1(filePath7);


        List<List<String>> content = new ArrayList<>();
       // content.addAll(importCSVData1);
        content.addAll(importCSVData2);
        content.addAll(importCSVData3);
        content.addAll(importCSVData4);
        content.addAll(importCSVData5);
        content.addAll(importCSVData6);
        content.addAll(importCSVData7);

        //导出不正常的数据（字段有空缺的）
        //excel标题，SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
        List<String> title = ShopExportExcelTitle.getBeeleStoreGoodsTitleList();
        String sheetName = "不正常数据记录表";
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExportExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        //excel文件名
        String fileName = "不正常数据记录表+2+3+4+5+6+7";

        ExportExcelUtil.exportExcel(fileName, response, wb, request);
        //dataImportService.importCSVData3(request, response);
        // dataImportService.importCSVData4(request, response);
    }

}
