package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.service.DataExportService;
import com.lbx.distribution.manageserver.service.DataImportService;
import com.lbx.distribution.manageserver.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Map;

/**
 * 数据导出
 */
@RestController
@RequestMapping(value = "/export")
public class DataExportController {

    @Autowired
    private DataExportService dataExportService;

    @GetMapping("/exportOrder")
    public void exportOrder(HttpServletRequest httpServletRequest, HttpServletResponse response){
        dataExportService.exportOrder(httpServletRequest, response );
    }

    @GetMapping("/exportShop")
    public void exportShop(HttpServletRequest httpServletRequest, HttpServletResponse response){
        dataExportService.exportShop( httpServletRequest, response );
    }

    @GetMapping("/exportShopModle")
    public void exportShopModle(HttpServletResponse response,  HttpServletRequest httpServletRequest){
        dataExportService.exportShopModle( response, httpServletRequest );
    }

    @GetMapping("/exportCompanyModle")
    public void exportCompanyModle(HttpServletResponse response,HttpServletRequest httpServletRequest){
        dataExportService.exportCompanyModle( response, httpServletRequest );
    }
}
