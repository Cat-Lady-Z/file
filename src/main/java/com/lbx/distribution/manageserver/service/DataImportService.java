package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.SimpleResponse;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.company.CompanyEntityVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopConfigImport;
import com.lbx.distribution.manageserver.entity.shop.ShopImport;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopChannelMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.ManageException;
import com.lbx.distribution.manageserver.util.excel.excel.ReadExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 数据导入
 */
@Service
public class DataImportService {

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ShopChannelMapper shopChannelMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyCommonService companyCommonService;

    //经纬度默认保存位数
    private final int scale = 6;
    /**
     * 导入门店
     */
    public SimpleResponse importShop(MultipartFile file,Integer companyId){
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行门店导入操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File toFile = null;
        try {
            //判断门店是否存在
            List<ShopImport> shopAllList = shopMapper.queryAllShopsToImport();
            //判断门店配置是否存在
            List<ShopConfigImport> shopConfigList = shopChannelMapper.queryAllShopConfig();
            Integer merchantId = null;
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
                return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }
            toFile = multipartFileToFile(file);
            CompanyEntity companyEntity = companyMapper.queryCompanyByCompanyId(companyId);
            if(companyEntity == null || companyEntity.getMerchantId() == null){
                logger.warn("未找到对应商户信息，请确认公司id是否正确");
                return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"未找到对应商户信息，请确认公司id是否正确");
            }else{
                merchantId = companyEntity.getMerchantId();
            }
            if(companyId == null){
                logger.warn("公司id不能为空！");
                return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"公司id不能为空！");
            }
            //File file = new File("E:\\门店导入.xlsx");
            //excel集合
            List<List<Object>> excelList =  ReadExcel.readExcel(toFile);
            //验证表头是否正确
            boolean resultBoolean = checkTitle(excelList.get(0));
            if(!resultBoolean){
                logger.warn("导入表头不正确，请确认！");
                return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"导入表头不正确，请确认！");
            }
            //新增门店的list
            List<ShopImport> addShopList = new ArrayList<>();
            //新增门店配置的list
            List<ShopConfigImport> addShopConfigList = new ArrayList<>();
            //遍历每一行
            for (int i=0;i<excelList.size();i++) {
                //去除表头
                if(i == 0) { continue; }
                //遍历每一个单元格,组装参数
                List<Object> shopImportList = excelList.get(i);
                if ("商户门店Id".equals(shopImportList.get(0).toString())) {
                    continue;
                }
                //获取新增门店集合
                addShopList = assembleShop(shopImportList,merchantId,companyId,addShopList);
                //组装门店配置集合
                addShopConfigList = assembleShopConfig(shopImportList,addShopConfigList);

            }
            if(addShopList.size() <= 0){
                return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入门店数量为0，请确认门店是否已存入");
            }
            //验证门店是否存在相关信息
            addShopList = checkShop(addShopList,addShopConfigList,shopAllList,shopConfigList);
            //存在数据不执行插入操作
            if(addShopList == null || addShopList.size() == 0){
                return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                insertShop(addShopList,addShopConfigList);
                //插入门店配置信息
                insertShopConfig(addShopConfigList,shopConfigList);
            }
        } catch (Exception e) {
            logger.error("导入门店异常！！");
            e.printStackTrace();
        }finally {
            if(toFile != null){
                toFile.delete();
            }
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行门店导入操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return simpleResponse;
    }

    /**
     * 组装门店参数
     * @param shopImportList
     * @param merchantId
     * @param companyId
     * @return
     */
    public List<ShopImport> assembleShop(List<Object> shopImportList,Integer merchantId,Integer companyId,List<ShopImport> addShopList){
        //查询集合中是否存在相同数据
        boolean exists = true;
        //判断是否存在相同数据
        for (ShopImport shopImport : addShopList) {
            String shopName = shopImport.getShopName();
            BigDecimal lng = shopImport.getLng();
            BigDecimal lat = shopImport.getLat();
            //门店名称相同，经纬度相同视为相同门店
            if(shopName.equals(shopImportList.get(3).toString()) && lng.compareTo(new BigDecimal(shopImportList.get(12).toString())) == 0
                    && lat.compareTo(new BigDecimal(shopImportList.get(13).toString())) == 0){
                exists = false;
            }
        }
        if(exists){
            ShopImport shop = new ShopImport();
            shop.setMerchantId(merchantId);
            shop.setCompanyId(companyId);
            shop.setOriginShopId(shopImportList.get(0).toString());
            shop.setShopName(shopImportList.get(3).toString());
            shop.setShopAddress(shopImportList.get(4).toString());
            shop.setShopType(shopImportList.get(5).toString());
            shop.setProvinceId(shopImportList.get(6).toString());
            shop.setProvinceName(shopImportList.get(7).toString());
            shop.setCityId(shopImportList.get(8).toString());
            shop.setCityName(shopImportList.get(9).toString());
            shop.setAreaId(shopImportList.get(10).toString());
            shop.setAreaName(shopImportList.get(11).toString());
            shop.setLng(BigDecimal.valueOf(Double.valueOf(shopImportList.get(12).toString())));
            shop.setLat(BigDecimal.valueOf(Double.valueOf(shopImportList.get(13).toString())));
            shop.setBusinessType(Integer.valueOf(shopImportList.get(14).toString()));
            shop.setContactName(shopImportList.get(15).toString());
            shop.setContactPhone(shopImportList.get(16).toString());
            addShopList.add(shop);
        }
        return addShopList;
    }

    /**
     * 组装门店配置
     * @param shopImportList
     * @return
     */
    public List<ShopConfigImport> assembleShopConfig(List<Object> shopImportList,List<ShopConfigImport> addShopConfigList){
        //查询集合中是否存在相同数据
        ShopConfigImport shopConfig = new ShopConfigImport();
        shopConfig.setChannelId(Integer.valueOf(shopImportList.get(1).toString()));
        shopConfig.setChannelShopId(shopImportList.get(2).toString());
        //shopName + lng + lat 作为查询相同门店的key
        String shopName = shopImportList.get(3).toString();
        String lng = BigDecimal.valueOf(Double.valueOf(shopImportList.get(12).toString())).setScale(scale,BigDecimal.ROUND_HALF_DOWN).toString();
        String lat = BigDecimal.valueOf(Double.valueOf(shopImportList.get(13).toString())).setScale(scale,BigDecimal.ROUND_HALF_DOWN).toString();
        String sign = shopName + lng + lat;
        shopConfig.setSign(sign);
        addShopConfigList.add(shopConfig);
        return addShopConfigList;
    }

    /**
     * 查询门店是否存在
     * @param importShopList  导入门店列表
     * @param addShopConfigList 导入门店配置列表
     * @param shopAllList  数据库门店列表
     * @param addShopConfigList 数据库门店配置列表
     */
    public List<ShopImport> checkShop(List<ShopImport> importShopList,List<ShopConfigImport> addShopConfigList,List<ShopImport> shopAllList,List<ShopConfigImport> shopConfigList){
        //循环表格里面的所有数据
        List<ShopImport> insertShopList = new ArrayList<>();
        for (ShopImport shop : importShopList) {
            String oldShopName = shop.getShopName();
            BigDecimal oldLng = shop.getLng();
            BigDecimal oldLat = shop.getLat();
            //门店标识
            String excelSign = oldShopName + oldLng.setScale(scale,BigDecimal.ROUND_HALF_DOWN) + oldLat.setScale(scale,BigDecimal.ROUND_HALF_DOWN);
            //循环表中所有的门店集合
            for (ShopImport shopImport : shopAllList) {
                String shopName = shopImport.getShopName();
                BigDecimal lat = shopImport.getLat();
                BigDecimal lng =  shopImport.getLng();
                String dataSign = shopName + lng.setScale(scale,BigDecimal.ROUND_HALF_DOWN) + lat.setScale(scale,BigDecimal.ROUND_HALF_DOWN);
                //校验门店名称，经纬度是否相同（判断门店是否存在）
                if(excelSign.equals(dataSign)){
                    Integer shopId = shopImport.getShopId();
                    for (ShopConfigImport shopConfig : addShopConfigList) {
                        if(excelSign.equals(shopConfig.getSign())){
                            //插入对应门店配置的shopId
                            shopConfig.setShopId(shopId);
                            //判断配置是否存在
                            for (ShopConfigImport shopConfigImport:shopConfigList) {
                                Integer channelId = shopConfigImport.getChannelId();
                                Integer configShopId = shopConfigImport.getShopId();
                                if(configShopId.compareTo(shopConfig.getShopId()) == 0 && channelId.compareTo(shopConfig.getChannelId()) == 0){
                                    insertShopList.clear();
                                    return insertShopList;
                                }
                            }
                        }
                    }
                }
            }
            //门店不存加入后续新增的list
            insertShopList.add(shop);
        }
        return insertShopList;
    }

    /**
     * 新增门店
     * @param addShopList
     */
    public void insertShop(List<ShopImport> addShopList,List<ShopConfigImport> addShopConfigList){
        for (ShopImport shop : addShopList) {
            BigDecimal oldLng = shop.getLng();
            String oldShopName = shop.getShopName();
            BigDecimal oldLat = shop.getLat();
            //门店标识
            String excelSign = oldShopName + oldLng.setScale(scale,BigDecimal.ROUND_HALF_DOWN) + oldLat.setScale(scale,BigDecimal.ROUND_HALF_DOWN);
            shopMapper.insertShopImport(shop);
            //插入对应门店配置的shopId
            for (ShopConfigImport shopConfigImport : addShopConfigList) {
                if(excelSign.equals(shopConfigImport.getSign())){
                    shopConfigImport.setShopId(shop.getShopId());
                }
            }
        }

    }

    /**
     * 校验并插入门店配置信息
     * @param addShopConfigList
     */
    public void insertShopConfig(List<ShopConfigImport> addShopConfigList,List<ShopConfigImport> shopConfigList){
        for (ShopConfigImport shopConfig : addShopConfigList) {
            //默认门店配置不存在
            boolean exists = true;
            //判断配置是否存在
            for (ShopConfigImport shopConfigImport : shopConfigList) {
                Integer shopId = shopConfigImport.getShopId();
                Integer channelId = shopConfigImport.getChannelId();
                if(shopId.compareTo(shopConfig.getShopId()) == 0 && channelId.compareTo(shopConfig.getChannelId()) == 0){
                    exists = false;
                }
            }
            //不存在才去添加
            if(exists){
                shopChannelMapper.insertShopConfig(shopConfig);
            }
        }
    }

    /**
     * 校验表头是否正确
     * @return
     */
    public boolean checkTitle(List<Object> excelTitle){
        String[] vailTitle = {"商户门店Id","渠道id","渠道门店id","门店名称","门店地址","门店类型","省份编码","省份名称","城市编码",
                "城市名称","县区编码","县区名称","经度","纬度","业务类型","联系人名称","联系人电话"};
        for(int i=0;i<vailTitle.length;i++){
            System.out.println(vailTitle[i] + " -- " + excelTitle.get(i));
            if(!vailTitle[i].equals(excelTitle.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file ) throws Exception {
        File toFile = null;
        InputStream ins = null;
        ins = file.getInputStream();
        toFile = new File(file.getOriginalFilename());
        inputStreamToFile(ins, toFile);
        if(ins != null){
            ins.close();
        }
        return toFile;
    }

    /**
     * 保存到当前根目录下
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            if(os != null){
                os.close();
            }
            if(ins != null){
                ins.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常返回
     * @param response
     * @param manageResultCode
     * @return
     */
    private SimpleResponse resultExceptionMsg(SimpleResponse response, ManageResultCode manageResultCode,String data){
        response.setCode(manageResultCode.getCode());
        response.setMsg(manageResultCode.getMessage());
        response.setData(data);
        logger.warn(manageResultCode.getMessage());
        return response;
    }

    /**
     * 公司导入(商户导入或公司导入)
     * @param file
     * @param parentId
     * @param merchantId
     * @return
     */
    @Transactional
    public SimpleResponse importCompany(MultipartFile file, Integer parentId, Integer merchantId) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行公司导入操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (parentId == null && merchantId == null){
            logger.warn("缺少父级公司id或商户id！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "缺少父级公司id或商户id!");
        }
        if(StringUtils.isEmpty(file)){
            logger.warn("文件不能为空!");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "文件不能为空!");
        }

        SimpleResponse simpleResponse = new SimpleResponse();
        long beginTime = new Date().getTime();
        File toFile = null;
        try {
            toFile = multipartFileToFile(file);

            List<List<Object>> excelList =  ReadExcel.readExcel(toFile);
            //验证表头是否正确
            boolean resultBoolean = this.checkCompanyExcelTitle(excelList.get(0));
            if(!resultBoolean){
                logger.warn("导入表头不正确，请确认!");
                return new SimpleResponse(ManageResultCode.DATA_REQUEST_ERROR.getCode(), "导入表头不正确，请确认!", null);
            }
            //新增公司的list
            List<CompanyEntity> addCompanyList = new ArrayList<>();
            //遍历每一行
            for (int i=0;i<excelList.size();i++) {
                //去除表头
                if(i == 0) { continue; }
                //遍历每一个单元格,组装参数
                List<Object> companyImportList =  excelList.get(i);
                if ("公司序号".equals(companyImportList.get(0).toString())) {
                    continue;
                }
                //获取新增公司集合
                addCompanyList = this.assembleCompany(companyImportList, merchantId, parentId, addCompanyList);
            }

            //表格的公司名称是否有重复的
            Set<String> companyNameSet = new HashSet<>();
            for (CompanyEntity company : addCompanyList) {
                String companyName = company.getCompanyName();
                //公司名称相同视为相同公司
                if (companyNameSet.contains(companyName)) {
                    logger.warn("导入表格有重复的公司名称!");
                    return new SimpleResponse(ManageResultCode.DATA_REQUEST_ERROR.getCode(), "导入表格有重复的公司名称!", null);
                }
                companyNameSet.add(companyName);
            }

            //添加门店相关信息
            this.insertCompany(addCompanyList);

        } catch (Exception e) {
            logger.error("导入公司异常！！");
            e.printStackTrace();
        }finally {
            if(toFile != null){
                toFile.delete();
            }
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行公司导入操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        return resultExceptionMsg(simpleResponse,ManageResultCode.SUCCESS,"导入公司成功！");
    }

    /**
     * 导入数据库
     * @param addCompanyList
     */
    private void insertCompany(List<CompanyEntity> addCompanyList) {
        for (CompanyEntity companyEntity:addCompanyList ) {
            companyCommonService.insertCompany(companyEntity);
        }
    }

    /**
     * 获取新增公司集合
     * @param companyImportList
     * @param merchantId
     * @param parentId
     * @param addCompanyList
     * @return
     */
    private List<CompanyEntity> assembleCompany(List<Object> companyImportList, Integer merchantId, Integer parentId, List<CompanyEntity> addCompanyList) {
        CompanyEntity company = new CompanyEntity();
        company.setMerchantId(merchantId);
        company.setParentId(parentId);

        String companyName = companyImportList.get(1).toString();
        company.setCompanyName(companyName);
        addCompanyList.add(company);

        return addCompanyList;
    }

    /**
     * 检验导入表格的表头是否正确
     * @param excelTitle
     * @return
     */
    private boolean checkCompanyExcelTitle(List<Object> excelTitle) {
        String[] vailTitle = {"公司序号","公司名称"};
        for(int i=0;i<vailTitle.length;i++){
            System.out.println(vailTitle[i] + " -- " + excelTitle.get(i));
            if(!vailTitle[i].equals(excelTitle.get(i))){
                return false;
            }
        }
        return true;
    }
}
