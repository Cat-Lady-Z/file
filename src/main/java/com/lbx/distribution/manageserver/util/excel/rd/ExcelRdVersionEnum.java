package com.lbx.distribution.manageserver.util.excel.rd;

/**
 * Description enum
 *
 */
public enum ExcelRdVersionEnum {

    /**
     * Excel 版本
     */
    XLS("03版本Excel"),
    XLSX("07+版本Excel");

    private String value;

    ExcelRdVersionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
