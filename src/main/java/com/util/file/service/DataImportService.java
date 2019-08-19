package com.util.file.service;
import com.util.file.entity.BeeleStoreGoods;
import com.util.file.entity.SimpleResponse;
import com.util.file.mapper.BeeleStoreGoodsMapper;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 数据导入
 */
@Service
public class DataImportService {

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);

    @Autowired
    private BeeleStoreGoodsMapper beeleStoreGoodsMapper;

    private final int batchNum = 150;

    /**
     * 将超大CSV文件导入MYSQL数据库
     * @return
     */
    public List<List<String>> importCSVData1(String filePath) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行导入:"+filePath+">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File file = null;
        List<List<String>> content = new ArrayList<>();
        try {
            //读取csv文件
            file = new File(filePath);
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
               // return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }

            //csv集合
            List<BeeleStoreGoods> addBeeleStoreGoodsList =  new ArrayList<>();
            List<BeeleStoreGoods> unNormalBeeleStoreGoodsList =  new ArrayList<>();
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);
            Charset charset = Charset.forName("GBK");
            CsvContainer csv = csvReader.read(file, charset);
            for (CsvRow row : csv.getRows()) {
                if (row.getOriginalLineNumber() != 1) {
                    //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
                    String rowField = row.getField(0);
                    String replace = rowField.replace("&reg;", " ");
                    String[] split = replace.split(";");
                    Boolean normalFlag = true;

                    BeeleStoreGoods beeleStoreGoods = new BeeleStoreGoods();
                    String skuIdTrim = split[0].trim();
                    beeleStoreGoods.setSkuId(Long.valueOf(skuIdTrim));
                    String upcTrim = split[1].trim();
                    beeleStoreGoods.setUpc(Long.valueOf(upcTrim));
                    String NAMETrim = split[2].trim();
                    beeleStoreGoods.setName(NAMETrim);

                    if (split.length != 7) {
                        normalFlag = false;
                    }

                    if (split.length>3) {
                        String DEPTIDTrim = split[3].trim();
                        Integer DEPTIDInteger = Integer.valueOf(DEPTIDTrim);
                        beeleStoreGoods.setStoreId(DEPTIDInteger);
                    }

                    if (split.length > 4) {
                        String STATUSTrim = split[4].trim();
                        beeleStoreGoods.setStatus(Short.valueOf(STATUSTrim));
                    }

                    if (split.length > 5) {
                        String UPC_TYPETrim = split[5].trim();
                        beeleStoreGoods.setUpcType(Short.valueOf(UPC_TYPETrim));
                    }

                    if (split.length >6) {
                        String GOODSIDTrim = split[6].trim();
                        beeleStoreGoods.setGoodsId(Integer.valueOf(GOODSIDTrim));
                    }

                    if (normalFlag) {
                        addBeeleStoreGoodsList.add(beeleStoreGoods);
                    } else {
                        unNormalBeeleStoreGoodsList.add(beeleStoreGoods);
                    }
                }
            }
            System.out.println("总行数：" + addBeeleStoreGoodsList.size());

            if(addBeeleStoreGoodsList.size() <= 0){
               // return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入beele_store_goods数量为0，请确认门店是否已存入");
            }
            //存在数据不执行插入操作
            if(addBeeleStoreGoodsList == null || addBeeleStoreGoodsList.size() == 0){
                logger.warn("存在数据不执行插入操作！");
                //  return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                //插入beele_store_goods
                int num = addBeeleStoreGoodsList.size() / batchNum;
                int i = 1;
                List<BeeleStoreGoods> batchList =  new ArrayList<>();
                for (BeeleStoreGoods beeleStoreGood:addBeeleStoreGoodsList) {
                    if (i <= batchNum) {
                        batchList.add(beeleStoreGood);
                        if (i == batchNum) {
                            beeleStoreGoodsMapper.insertBatch(batchList);
                        }
                        i++;
                    } else {
                        i = 1;
                        batchList.clear();
                        batchList.add(beeleStoreGood);
                        i++;
                    }
                }

                beeleStoreGoodsMapper.insertBatch(batchList);
            }

            //sheet名
            //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
            for (int i = 0; i < unNormalBeeleStoreGoodsList.size(); i++) {
                BeeleStoreGoods beeleStoreGoods = unNormalBeeleStoreGoodsList.get(i);
                List<String> item = new ArrayList<>();
                item.add(beeleStoreGoods.getSkuId()+ "");
                item.add(beeleStoreGoods.getUpc()+ "");
                item.add(beeleStoreGoods.getName()+ "");
                item.add(beeleStoreGoods.getStoreId()+ "");
                item.add(beeleStoreGoods.getStatus()+ "");
                item.add(beeleStoreGoods.getUpcType()+ "");
                item.add(beeleStoreGoods.getGoodsId()+ "");
                content.add(item);
            }
        } catch (Exception e) {
            logger.error("导入beele_store_goods异常！！");
            e.printStackTrace();
        }finally {
           /* if(file != null){
                file.delete();
            }*/
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"导入结束，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return content;
    }

    public List<List<String>> importCSVData2(String filePath) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"开始执行导入>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File file = null;
        List<List<String>> content = new ArrayList<>();
        try {
            //读取csv文件
            file = new File(filePath);
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
                // return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }

            //csv集合
            List<BeeleStoreGoods> addBeeleStoreGoodsList =  new ArrayList<>();
            List<BeeleStoreGoods> unNormalBeeleStoreGoodsList =  new ArrayList<>();
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);
            Charset charset = Charset.forName("GBK");
            CsvContainer csv = csvReader.read(file, charset);
            for (CsvRow row : csv.getRows()) {
                if (row.getOriginalLineNumber() != 1) {
                    //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
                    String rowField = row.getField(0);
                    String replace = rowField.replace("&reg;", " ");
                    String[] split = replace.split(";");
                    Boolean normalFlag = true;

                    BeeleStoreGoods beeleStoreGoods = new BeeleStoreGoods();
                    String skuIdTrim = split[0].trim();
                    beeleStoreGoods.setSkuId(Long.valueOf(skuIdTrim));
                    String upcTrim = split[1].trim();
                    beeleStoreGoods.setUpc(Long.valueOf(upcTrim));
                    String NAMETrim = split[2].trim();
                    beeleStoreGoods.setName(NAMETrim);
                    if (split.length>3) {
                        String DEPTIDTrim = split[3].trim();
                        Integer DEPTIDInteger = Integer.valueOf(DEPTIDTrim);
                        beeleStoreGoods.setStoreId(DEPTIDInteger);
                    } else {
                        normalFlag = false;
                    }
                    if (split.length > 4) {
                        String STATUSTrim = split[4].trim();
                        beeleStoreGoods.setStatus(Short.valueOf(STATUSTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (split.length > 5) {
                        String UPC_TYPETrim = split[5].trim();
                        beeleStoreGoods.setUpcType(Short.valueOf(UPC_TYPETrim));
                    } else {
                        normalFlag = false;
                    }
                    if (split.length >6) {
                        String GOODSIDTrim = split[6].trim();
                        beeleStoreGoods.setGoodsId(Integer.valueOf(GOODSIDTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (normalFlag) {
                        addBeeleStoreGoodsList.add(beeleStoreGoods);
                    } else {
                        unNormalBeeleStoreGoodsList.add(beeleStoreGoods);
                    }
                }
            }
            System.out.println("总行数：" + addBeeleStoreGoodsList.size());

            if(addBeeleStoreGoodsList.size() <= 0){
                // return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入beele_store_goods数量为0，请确认门店是否已存入");
            }
            //存在数据不执行插入操作
            if(addBeeleStoreGoodsList == null || addBeeleStoreGoodsList.size() == 0){
                logger.warn("存在数据不执行插入操作！");
                //  return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                //插入beele_store_goods
                int num = addBeeleStoreGoodsList.size() / batchNum;
                int i = 1;
                List<BeeleStoreGoods> batchList =  new ArrayList<>();
                for (BeeleStoreGoods beeleStoreGood:addBeeleStoreGoodsList) {
                    if (i <= batchNum) {
                        batchList.add(beeleStoreGood);
                        if (i == batchNum) {
                            beeleStoreGoodsMapper.insertBatch(batchList);
                        }
                        i++;
                    } else {
                        i = 1;
                        batchList.clear();
                        batchList.add(beeleStoreGood);
                        i++;
                    }
                }

                beeleStoreGoodsMapper.insertBatch(batchList);
            }

            //sheet名
            //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
            for (int i = 0; i < unNormalBeeleStoreGoodsList.size(); i++) {
                BeeleStoreGoods beeleStoreGoods = unNormalBeeleStoreGoodsList.get(i);
                List<String> item = new ArrayList<>();
                item.add(beeleStoreGoods.getSkuId()+ "");
                item.add(beeleStoreGoods.getUpc()+ "");
                item.add(beeleStoreGoods.getName()+ "");
                item.add(beeleStoreGoods.getStoreId()+ "");
                item.add(beeleStoreGoods.getStatus()+ "");
                item.add(beeleStoreGoods.getUpcType()+ "");
                item.add(beeleStoreGoods.getGoodsId()+ "");
                content.add(item);
            }
        } catch (Exception e) {
            logger.error("导入beele_store_goods异常！！");
            e.printStackTrace();
        }finally {
           /* if(file != null){
                file.delete();
            }*/
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"导入结束，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return content;
    }

    public List<List<String>> importCSVData3(String filePath) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+": 开始执行导入>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File file = null;
        List<List<String>> content = new ArrayList<>();
        try {
            //读取csv文件
            file = new File(filePath);
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
                // return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }

            //csv集合
            List<BeeleStoreGoods> addBeeleStoreGoodsList =  new ArrayList<>();
            List<BeeleStoreGoods> unNormalBeeleStoreGoodsList =  new ArrayList<>();
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);
            Charset charset = Charset.forName("GBK");
            CsvContainer csv = csvReader.read(file, charset);
            for (CsvRow row : csv.getRows()) {
                if (row.getOriginalLineNumber() != 1) {
                    //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
                    String rowField = row.getField(0);
                    String replace = rowField.replace("&reg;", " ");
                    String[] split = replace.split(";");
                    Boolean normalFlag = true;

                    BeeleStoreGoods beeleStoreGoods = new BeeleStoreGoods();
                    String skuIdTrim = split[0].trim();
                    beeleStoreGoods.setSkuId(Long.valueOf(skuIdTrim));
                    String upcTrim = split[1].trim();
                    beeleStoreGoods.setUpc(Long.valueOf(upcTrim));
                    String NAMETrim = split[2].trim();
                    beeleStoreGoods.setName(NAMETrim);
                    if (split.length>3) {
                        String DEPTIDTrim = split[3].trim();
                        Integer DEPTIDInteger = Integer.valueOf(DEPTIDTrim);
                        beeleStoreGoods.setStoreId(DEPTIDInteger);
                    } else {
                        normalFlag = false;
                    }
                    if (split.length > 4) {
                        String STATUSTrim = split[4].trim();
                        beeleStoreGoods.setStatus(Short.valueOf(STATUSTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (split.length > 5) {
                        String UPC_TYPETrim = split[5].trim();
                        beeleStoreGoods.setUpcType(Short.valueOf(UPC_TYPETrim));
                    } else {
                        normalFlag = false;
                    }
                    if (split.length >6) {
                        String GOODSIDTrim = split[6].trim();
                        beeleStoreGoods.setGoodsId(Integer.valueOf(GOODSIDTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (normalFlag) {
                        addBeeleStoreGoodsList.add(beeleStoreGoods);
                    } else {
                        unNormalBeeleStoreGoodsList.add(beeleStoreGoods);
                    }
                }
            }
            System.out.println("总行数：" + addBeeleStoreGoodsList.size());

            if(addBeeleStoreGoodsList.size() <= 0){
                // return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入beele_store_goods数量为0，请确认门店是否已存入");
            }
            //存在数据不执行插入操作
            if(addBeeleStoreGoodsList == null || addBeeleStoreGoodsList.size() == 0){
                logger.warn("存在数据不执行插入操作！");
                //  return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                //插入beele_store_goods
                int num = addBeeleStoreGoodsList.size() / batchNum;
                int i = 1;
                List<BeeleStoreGoods> batchList =  new ArrayList<>();
                for (BeeleStoreGoods beeleStoreGood:addBeeleStoreGoodsList) {
                    if (i <= batchNum) {
                        batchList.add(beeleStoreGood);
                        if (i == batchNum) {
                            beeleStoreGoodsMapper.insertBatch(batchList);
                        }
                        i++;
                    } else {
                        i = 1;
                        batchList.clear();
                        batchList.add(beeleStoreGood);
                        i++;
                    }
                }

                beeleStoreGoodsMapper.insertBatch(batchList);
            }

            //sheet名
            //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
            for (int i = 0; i < unNormalBeeleStoreGoodsList.size(); i++) {
                BeeleStoreGoods beeleStoreGoods = unNormalBeeleStoreGoodsList.get(i);
                List<String> item = new ArrayList<>();
                item.add(beeleStoreGoods.getSkuId()+ "");
                item.add(beeleStoreGoods.getUpc()+ "");
                item.add(beeleStoreGoods.getName()+ "");
                item.add(beeleStoreGoods.getStoreId()+ "");
                item.add(beeleStoreGoods.getStatus()+ "");
                item.add(beeleStoreGoods.getUpcType()+ "");
                item.add(beeleStoreGoods.getGoodsId()+ "");
                content.add(item);
            }
        } catch (Exception e) {
            logger.error("导入beele_store_goods异常！！");
            e.printStackTrace();
        }finally {
           /* if(file != null){
                file.delete();
            }*/
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"导入结束，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return content;
    }

    public List<List<String>> importCSVData4(String filePath) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+":开始执行导入>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File file = null;
        List<List<String>> content = new ArrayList<>();
        try {
            //读取csv文件
            file = new File(filePath);
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
                // return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }

            //csv集合
            List<BeeleStoreGoods> addBeeleStoreGoodsList =  new ArrayList<>();
            List<BeeleStoreGoods> unNormalBeeleStoreGoodsList =  new ArrayList<>();
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);
            Charset charset = Charset.forName("GBK");
            CsvContainer csv = csvReader.read(file, charset);
            for (CsvRow row : csv.getRows()) {
                if (row.getOriginalLineNumber() != 1) {
                    //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
                    String rowField = row.getField(0);
                    String replace = rowField.replace("&reg;", " ");
                    String[] split = replace.split(";");
                    Boolean normalFlag = true;

                    BeeleStoreGoods beeleStoreGoods = new BeeleStoreGoods();
                    String skuIdTrim = split[0].trim();
                    beeleStoreGoods.setSkuId(Long.valueOf(skuIdTrim));
                    String upcTrim = split[1].trim();
                    beeleStoreGoods.setUpc(Long.valueOf(upcTrim));
                    String NAMETrim = split[2].trim();
                    beeleStoreGoods.setName(NAMETrim);
                    if (split.length>3) {
                        String DEPTIDTrim = split[3].trim();
                        Integer DEPTIDInteger = Integer.valueOf(DEPTIDTrim);
                        beeleStoreGoods.setStoreId(DEPTIDInteger);
                    } else {
                        normalFlag = false;
                    }
                    if (split.length > 4) {
                        String STATUSTrim = split[4].trim();
                        beeleStoreGoods.setStatus(Short.valueOf(STATUSTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (split.length > 5) {
                        String UPC_TYPETrim = split[5].trim();
                        beeleStoreGoods.setUpcType(Short.valueOf(UPC_TYPETrim));
                    } else {
                        normalFlag = false;
                    }
                    if (split.length >6) {
                        String GOODSIDTrim = split[6].trim();
                        beeleStoreGoods.setGoodsId(Integer.valueOf(GOODSIDTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (normalFlag) {
                        addBeeleStoreGoodsList.add(beeleStoreGoods);
                    } else {
                        unNormalBeeleStoreGoodsList.add(beeleStoreGoods);
                    }
                }
            }
            System.out.println("总行数：" + addBeeleStoreGoodsList.size());

            if(addBeeleStoreGoodsList.size() <= 0){
                // return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入beele_store_goods数量为0，请确认门店是否已存入");
            }
            //存在数据不执行插入操作
            if(addBeeleStoreGoodsList == null || addBeeleStoreGoodsList.size() == 0){
                logger.warn("存在数据不执行插入操作！");
                //  return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                //插入beele_store_goods
                int num = addBeeleStoreGoodsList.size() / batchNum;
                int i = 1;
                List<BeeleStoreGoods> batchList =  new ArrayList<>();
                for (BeeleStoreGoods beeleStoreGood:addBeeleStoreGoodsList) {
                    if (i <= batchNum) {
                        batchList.add(beeleStoreGood);
                        if (i == batchNum) {
                            beeleStoreGoodsMapper.insertBatch(batchList);
                        }
                        i++;
                    } else {
                        i = 1;
                        batchList.clear();
                        batchList.add(beeleStoreGood);
                        i++;
                    }
                }

                beeleStoreGoodsMapper.insertBatch(batchList);
            }

            //sheet名
            //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
            for (int i = 0; i < unNormalBeeleStoreGoodsList.size(); i++) {
                BeeleStoreGoods beeleStoreGoods = unNormalBeeleStoreGoodsList.get(i);
                List<String> item = new ArrayList<>();
                item.add(beeleStoreGoods.getSkuId()+ "");
                item.add(beeleStoreGoods.getUpc()+ "");
                item.add(beeleStoreGoods.getName()+ "");
                item.add(beeleStoreGoods.getStoreId()+ "");
                item.add(beeleStoreGoods.getStatus()+ "");
                item.add(beeleStoreGoods.getUpcType()+ "");
                item.add(beeleStoreGoods.getGoodsId()+ "");
                content.add(item);
            }
        } catch (Exception e) {
            logger.error("导入beele_store_goods异常！！");
            e.printStackTrace();
        }finally {
           /* if(file != null){
                file.delete();
            }*/
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"导入结束，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return content;
    }

    public List<List<String>> importCSVData5(String filePath) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+"开始执行导入>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
        long beginTime = new Date().getTime();
        SimpleResponse simpleResponse = new SimpleResponse();
        File file = null;
        List<List<String>> content = new ArrayList<>();
        try {
            //读取csv文件
            file = new File(filePath);
            if(StringUtils.isEmpty(file)){
                logger.warn("文件不能为空！");
                // return resultExceptionMsg(simpleResponse,ManageResultCode.NULL_Exception,"文件不能为空！");
            }

            //csv集合
            List<BeeleStoreGoods> addBeeleStoreGoodsList =  new ArrayList<>();
            List<BeeleStoreGoods> unNormalBeeleStoreGoodsList =  new ArrayList<>();
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);
            Charset charset = Charset.forName("GBK");
            CsvContainer csv = csvReader.read(file, charset);
            for (CsvRow row : csv.getRows()) {
                if (row.getOriginalLineNumber() != 1) {
                    //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
                    String rowField = row.getField(0);
                    String replace = rowField.replace("&reg;", " ");
                    String[] split = replace.split(";");
                    Boolean normalFlag = true;

                    BeeleStoreGoods beeleStoreGoods = new BeeleStoreGoods();
                    String skuIdTrim = split[0].trim();
                    beeleStoreGoods.setSkuId(Long.valueOf(skuIdTrim));
                    String upcTrim = split[1].trim();
                    beeleStoreGoods.setUpc(Long.valueOf(upcTrim));
                    String NAMETrim = split[2].trim();
                    beeleStoreGoods.setName(NAMETrim);
                    if (split.length>3) {
                        String DEPTIDTrim = split[3].trim();
                        Integer DEPTIDInteger = Integer.valueOf(DEPTIDTrim);
                        beeleStoreGoods.setStoreId(DEPTIDInteger);
                    } else {
                        normalFlag = false;
                    }
                    if (split.length > 4) {
                        String STATUSTrim = split[4].trim();
                        beeleStoreGoods.setStatus(Short.valueOf(STATUSTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (split.length > 5) {
                        String UPC_TYPETrim = split[5].trim();
                        beeleStoreGoods.setUpcType(Short.valueOf(UPC_TYPETrim));
                    } else {
                        normalFlag = false;
                    }
                    if (split.length >6) {
                        String GOODSIDTrim = split[6].trim();
                        beeleStoreGoods.setGoodsId(Integer.valueOf(GOODSIDTrim));
                    } else {
                        normalFlag = false;
                    }

                    if (normalFlag) {
                        addBeeleStoreGoodsList.add(beeleStoreGoods);
                    } else {
                        unNormalBeeleStoreGoodsList.add(beeleStoreGoods);
                    }
                }
            }
            System.out.println("总行数：" + addBeeleStoreGoodsList.size());

            if(addBeeleStoreGoodsList.size() <= 0){
                // return resultExceptionMsg(simpleResponse,ManageResultCode.DATA_REQUEST_ERROR,"可导入beele_store_goods数量为0，请确认门店是否已存入");
            }
            //存在数据不执行插入操作
            if(addBeeleStoreGoodsList == null || addBeeleStoreGoodsList.size() == 0){
                logger.warn("存在数据不执行插入操作！");
                //  return resultExceptionMsg(simpleResponse,ManageResultCode.REQUEST_FAIL,"门店信息已存在或门店配置已存在");
            }else{
                //插入beele_store_goods
                int num = addBeeleStoreGoodsList.size() / batchNum;
                int i = 1;
                List<BeeleStoreGoods> batchList =  new ArrayList<>();
                for (BeeleStoreGoods beeleStoreGood:addBeeleStoreGoodsList) {
                    if (i <= batchNum) {
                        batchList.add(beeleStoreGood);
                        if (i == batchNum) {
                            beeleStoreGoodsMapper.insertBatch(batchList);
                        }
                        i++;
                    } else {
                        i = 1;
                        batchList.clear();
                        batchList.add(beeleStoreGood);
                        i++;
                    }
                }

                beeleStoreGoodsMapper.insertBatch(batchList);
            }

            //sheet名
            //SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID
            for (int i = 0; i < unNormalBeeleStoreGoodsList.size(); i++) {
                BeeleStoreGoods beeleStoreGoods = unNormalBeeleStoreGoodsList.get(i);
                List<String> item = new ArrayList<>();
                item.add(beeleStoreGoods.getSkuId()+ "");
                item.add(beeleStoreGoods.getUpc()+ "");
                item.add(beeleStoreGoods.getName()+ "");
                item.add(beeleStoreGoods.getStoreId()+ "");
                item.add(beeleStoreGoods.getStatus()+ "");
                item.add(beeleStoreGoods.getUpcType()+ "");
                item.add(beeleStoreGoods.getGoodsId()+ "");
                content.add(item);
            }
        } catch (Exception e) {
            logger.error("导入beele_store_goods异常！！");
            e.printStackTrace();
        }finally {
           /* if(file != null){
                file.delete();
            }*/
        }
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>"+filePath+":导入结束，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return content;
    }
}
