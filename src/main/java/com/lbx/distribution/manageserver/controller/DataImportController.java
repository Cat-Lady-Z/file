package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.SimpleResponse;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.DataImportService;
import com.lbx.distribution.manageserver.util.ManageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数据导入
 */
@RestController
@RequestMapping(value = "/import")
public class DataImportController {

    @Autowired
    private DataImportService dataImportService;
    @Autowired
    private CommonHelper commonHelper;

    @RequestMapping(value="/importShop",method= RequestMethod.POST)
    public SimpleResponse importShop(@RequestParam("file") MultipartFile file, Integer companyId){
        //判断上级公司是否在启用状态，禁用则不允许导入
        commonHelper.isAvailableHighLevelStatus(null, companyId);

        return dataImportService.importShop(file,companyId);
    }

    @RequestMapping(value="/importCompany",method= RequestMethod.POST)
    public SimpleResponse importCompany(@RequestParam("file") MultipartFile file, Integer companyId, Integer merchantId){
        //判断上级机构（商户/公司）是否在启用状态，，禁用则不允许导入
        commonHelper.isAvailableHighLevelStatus(merchantId, companyId);

        return dataImportService.importCompany(file,companyId, merchantId);
    }
}
