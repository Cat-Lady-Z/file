package com.lbx.distribution.manageserver.util.excel.rd;

import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ExcelRdUtil {

    protected static Object getCellValue(XSSFCell cell, ExcelRdTypeEnum type) {
        if (cell == null || "".equals(cell.toString().trim())) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        if (type == ExcelRdTypeEnum.INTEGER && cellType == CellType.NUMERIC) {
            Double numeric = cell.getNumericCellValue();
            return numeric.intValue();
        }
        if (type == ExcelRdTypeEnum.LONG && cellType == CellType.NUMERIC) {
            Double numeric = cell.getNumericCellValue();
            return numeric.longValue();
        }
        if (type == ExcelRdTypeEnum.DOUBLE && cellType == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        if (type == ExcelRdTypeEnum.DATETIME && cellType == CellType.NUMERIC) {
            return cell.getDateCellValue();
        }
        if (type == ExcelRdTypeEnum.DATE && cellType == CellType.NUMERIC) {
            return new java.sql.Date(cell.getDateCellValue().getTime());
        }
        if (type == ExcelRdTypeEnum.STRING && cellType == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return cell.toString();
    }

    protected static Object getCellValue(HSSFCell cell, ExcelRdTypeEnum type) {
        if (cell == null || "".equals(cell.toString().trim())) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (type == ExcelRdTypeEnum.INTEGER && cellType == CellType.NUMERIC) {
            Double numeric = cell.getNumericCellValue();
            return numeric.intValue();
        }
        if (type == ExcelRdTypeEnum.LONG && cellType == CellType.NUMERIC) {
            Double numeric = cell.getNumericCellValue();
            return numeric.longValue();
        }
        if (type == ExcelRdTypeEnum.DOUBLE && cellType == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        if (type == ExcelRdTypeEnum.DATETIME && cellType == CellType.NUMERIC) {
            return cell.getDateCellValue();
        }
        if (type == ExcelRdTypeEnum.DATE && cellType == CellType.NUMERIC) {
            return new java.sql.Date(cell.getDateCellValue().getTime());
        }
        if (type == ExcelRdTypeEnum.STRING && cellType == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return cell.getStringCellValue();
    }


    public static List<ShopEntityVo> getShopList(MultipartFile multipartFile) throws IOException, ExcelRdException {
        String fileRealName = multipartFile.getOriginalFilename();//获得原始文件名;
        /*int pointIndex =  fileRealName.lastIndexOf(".");//点号的位置
        String fileSuffix = fileRealName.substring(pointIndex);//截取文件后缀
        String fileNewName = DateUtils.getNowTimeForUpload();//文件new名称时间戳
        String saveFileName = fileNewName.concat(fileSuffix);//文件存取名*/
        String filePath  = "D:\\FileAll" ;
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            path.mkdirs();
        }
        File savedFile = new File(filePath);
        boolean isCreateSuccess = savedFile.createNewFile(); // 是否创建文件成功
        if(isCreateSuccess){      //将文件写入
            //第一种
            multipartFile.transferTo(savedFile);
        }

        ExcelRd excelRd = new ExcelRd(savedFile);
        excelRd.setStartRow(1);    // 指定起始行，下标从0开始计数
        excelRd.setStartCol(0);    // 指定起始列，下标从0开始计数
        ExcelRdTypeEnum[] types = {
                ExcelRdTypeEnum.INTEGER,
                ExcelRdTypeEnum.LONG,
                ExcelRdTypeEnum.DOUBLE,
                ExcelRdTypeEnum.DATETIME,
                ExcelRdTypeEnum.DATE,
                ExcelRdTypeEnum.STRING
        };
        excelRd.setTypes(types);    // 指定每列的类型

        List<List<Object>> rows = excelRd.analysisXlsx();

        List<ShopEntityVo> shopEntities = null;
        return shopEntities;
    }
}
