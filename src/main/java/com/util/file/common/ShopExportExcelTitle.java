package com.util.file.common;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: 门店导出表表头
 * @Description: //
 */
public class ShopExportExcelTitle {

    public static final String titleString = "商户门店Id,物流分发平台门店Id,门店名称,创建日期";

    public static final String titleKeyString = "origin_shop_id,shop_id,shop_name,create_time";

    public static final String BeeleStoreGoodsTitleKeyString = "SKU_ID;UPC;NAME;DEPTID;STATUS;UPC_TYPE;GOODSID";

    public static List<String> getTitleList(){
        List<String> titleList = Arrays.asList(titleString.split(","));
        return titleList;
    }

    public static List<String> getTitleKey(){
        List<String> titleKey = Arrays.asList(titleKeyString.split(","));
        return titleKey;
    }

    public static List<String> getBeeleStoreGoodsTitleList() {
        List<String> titleList = Arrays.asList(BeeleStoreGoodsTitleKeyString.split(","));
        return titleList;
    }
}
