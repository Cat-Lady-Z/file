package com.lbx.distribution.manageserver.util.excel.export;

//import lombok.Data;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
//@Data
public abstract class ExcelContent {

    /**
     * excel 工作簿
     */
    protected SXSSFWorkbook workbook;
    /** sheet */
    /**
     * 多 sheet 支持
     */
    protected SXSSFSheet sheet;
    protected List<SXSSFSheet> sheets;
    /**
     * sheet号
     */
    protected Integer sheetNum = 0;
    /**
     * 行号
     */
    protected Integer rowNum = 0;
    /** 基本信息后的第一行。 */

    /**
     * 标题
     */
    protected String title;
    /**
     * 创建人
     */
    protected String createBy;
    /**
     * 时间从
     */
    protected String dateFrom;
    /**
     * 时间从
     */
    protected String dateTo;
    /**
     * 保存路径
     */
    protected String savePath;
    /**
     * 表格列名
     */
    protected List<String> header;
    /**
     * Excel 宽度【用于在没有title的情况下定义标题合并】
     */
    protected Integer width;
    /**
     * 行对象
     */
    protected List<ExcelRow> rows;

    /**
     * 字体缓存重用
     */
    protected Map<String, Font> workBookFonts;

    /**
     * 其他参数
     */
    protected Map<String, Object> params;

    /**
     * 所有样式
     */
    protected ExcelStyle style;

    protected Integer cacheRowsInMemory = 10240;

    protected List<SXSSFSheet> getSheets() {
        if (this.sheets == null) {
            this.sheets = new ArrayList<SXSSFSheet>();
        }
        return this.sheets;
    }

    protected void addSheet(SXSSFSheet sheet) {
        if (this.sheets == null) {
            this.sheets = new ArrayList<SXSSFSheet>();
        }

        // 不显示风格线
        sheet.setDisplayGridlines(false);
        PrintSetup ps = sheet.getPrintSetup();
        // 打印方向，true：横向，false：纵向(默认)
        ps.setLandscape(false);
        // A4纸
        ps.setPaperSize(PrintSetup.A4_PAPERSIZE);
        this.sheets.add(sheet);
    }

    public void setCreateBy(String createBy) {
        if (createBy == null) {
            createBy = "";
        }
        this.createBy = createBy;
    }

    public void setCreateBy(Object createBy) {
        if (createBy == null) {
            createBy = "";
        }
        this.createBy = createBy.toString();
    }

    /**
     * 使用List<String>初始化列名
     */
    public void setHeader(List<String> header) {
        if (header != null) {
            this.width = header.size();
        }
        this.header = header;
    }

    /**
     * 使用String[] 初始化列名
     */
    public void setHeader(String[] header) {
        if (header != null) {
            this.width = header.length;
        }
        this.header = new ArrayList<String>();
        for (String h : header) {
            this.header.add(h);
        }
    }

    protected List<ExcelRow> getRows() {
        if (rows == null) {
            rows = new ArrayList<ExcelRow>();
        }
        return rows;
    }

    protected void addRow(ExcelRow row) {
        if (this.rows == null) {
            this.rows = new ArrayList<ExcelRow>();
        }
        this.rows.add(row);
    }

    //待完善
    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(SXSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public Map<String, Font> getWorkBookFonts() {
        return workBookFonts;
    }

    public void setWorkBookFonts(Map<String, Font> workBookFonts) {
        this.workBookFonts = workBookFonts;
    }

    public ExcelStyle getStyle() {
        return style;
    }

    public void setStyle(ExcelStyle style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
