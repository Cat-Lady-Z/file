package com.lbx.distribution.manageserver.util.excel.export;

import com.lbx.distribution.manageserver.entity.order.OrderVo;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 左对齐
     */
    public static final HorizontalAlignment ALIGN_LEFT = HorizontalAlignment.LEFT;
    /**
     * 中对齐
     */
    public static final HorizontalAlignment ALIGN_CENTER = HorizontalAlignment.CENTER;
    /**
     * 右对齐
     */
    public static final HorizontalAlignment ALIGN_RIGHT = HorizontalAlignment.RIGHT;
    /**
     * 边框大小
     */
    public static final BorderStyle BORDER = BorderStyle.THIN;


    /**
     * 默认颜色
     */
    private static final short DEFAULT_COLOR = Font.COLOR_NORMAL;
    /**
     * 默认高度
     */
    private static final short DEFAULT_HEIGHT_IN_POINTS = 10;
    /**
     * 默认字体
     */
    private static final String DEFAULT_FONT_NAME = "宋体";


    /**
     * 年月日，时分秒
     */
    protected static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 年月日
     */
    protected static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Description 判断字符是否为单字节字符
     *
     * @param c c
     * @return int
     */
    private static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * Description 判断长度
     *
     * @param s s
     * @return int
     */
    private static int length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    // 判断宽度
    private static int width(String s) {
        return (length(s) + 2) * 256;
    }

    /**
     * Description 自动设置shell的col宽度。如果超过宽度，返回真
     * create @ 2017-07-15 21:10:16
     *
     * @param sheel sheel
     * @param col   col
     * @param s     s
     * @return boolean
     * @author wangkc admin@wkclz.com
     */
    protected static boolean setWidth(SXSSFSheet sheel, int col, String s) {
        int width = ExcelUtil.width(s);
        width = width > 72 * 256 ? 72 * 256 : width;
        if (width > sheel.getColumnWidth(col)) {
            sheel.setColumnWidth(col, width);
        }
        return width == 72 * 256;
    }


    /**
     * Description 使用标准时间换取年月日的时间
     * create @ 2017-07-14 21:18:09
     *
     * @param date date
     * @author wangkc admin@wkclz.com
     */
    public static java.sql.Date getSqlDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }


    /**
     * Description 构建字体
     *
     * @param excel excel
     * @return Font
     */
    protected static Font createFont(Excel excel) {
        return createFont(excel, 0, 0, null);
    }

    /**
     * Description 构建字体
     *
     * @param excel          excel
     * @param heightInPoints heightInPoints
     * @return Font
     */
    protected static Font createFont(Excel excel, int heightInPoints) {
        return createFont(excel, 0, heightInPoints, null);
    }

    /**
     * Description 构建字体
     *
     * @param excel          excel
     * @param color          color
     * @param heightInPoints heightInPoints
     * @param fontName       fontName
     * @return Font
     */
    protected static Font createFont(Excel excel, int color, int heightInPoints, String fontName) {
        SXSSFWorkbook workbook = excel.getWorkbook();
        String key = workbook.toString() + "_" + color + "_" + heightInPoints + "_" + fontName;
        Map<String, Font> fonts = excel.getWorkBookFonts();

        if (fonts == null) {
            fonts = new HashMap<String, Font>();
        }
        Font font = fonts.get(key);
        if (font != null) {
            return font;
        }

        font = workbook.createFont();
        font.setColor(color == 0 ? DEFAULT_COLOR : (short) color);
        font.setFontHeightInPoints(heightInPoints == 0 ? DEFAULT_HEIGHT_IN_POINTS : (short) heightInPoints);
        font.setFontName(fontName == null ? DEFAULT_FONT_NAME : fontName);

        // 缓存
        fonts.put(key, font);
        excel.setWorkBookFonts(fonts);

        return font;
    }


    /**
     * @param excel  excel
     * @param cell   cell
     * @param align  align
     * @param border border
     */
    protected static void setIntStrStyle(Excel excel, SXSSFCell cell, HorizontalAlignment align, boolean border) {
        ExcelStyle style = excel.getStyle();
        cell.setCellStyle(style.getStyleStrCenterWithBorder(excel));
        // 边框 + 左边
        if (border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleStrLeftWithBorder(excel));
        }
        // 无边框 + 左边
        if (!border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleStrLeftNoBorder(excel));
        }
        // 无边框 + 中间
        if (!border && HorizontalAlignment.CENTER == align) {
            cell.setCellStyle(style.getStyleStrCenterNoBorder(excel));
        }
    }

    /**
     * @param excel  excel
     * @param cell   cell
     * @param align  align
     * @param border border
     */
    protected static void setDoubleStyle(Excel excel, SXSSFCell cell, HorizontalAlignment align, boolean border) {
        ExcelStyle style = excel.getStyle();
        cell.setCellStyle(style.getStyleNumCenterWithBorder(excel));
        // 边框 + 左边
        if (border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleNumLeftWithBorder(excel));
        }
        // 无边框 + 左边
        if (!border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleNumLeftNoBorder(excel));
        }
        // 无边框 + 中间
        if (!border && HorizontalAlignment.CENTER == align) {
            cell.setCellStyle(style.getStyleNumCenterNoBorder(excel));
        }
    }

    /**
     * @param excel  excel
     * @param cell   cell
     * @param align  align
     * @param border border
     */
    protected static void setDateStyle(Excel excel, SXSSFCell cell, HorizontalAlignment align, boolean border) {
        ExcelStyle style = excel.getStyle();
        cell.setCellStyle(style.getStyleDateCenterWithBorder(excel));
        // 边框 + 左边
        if (border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleDateLeftWithBorder(excel));
        }
        // 无边框 + 左边
        if (!border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleDateLeftNoBorder(excel));
        }
        // 无边框 + 中间
        if (!border && HorizontalAlignment.CENTER == align) {
            cell.setCellStyle(style.getStyleDateCenterNoBorder(excel));
        }
    }

    /**
     * @param excel  excel
     * @param cell   cell
     * @param align  align
     * @param border border
     */
    protected static void setDateTimeStyle(Excel excel, SXSSFCell cell, HorizontalAlignment align, boolean border) {
        ExcelStyle style = excel.getStyle();
        cell.setCellStyle(style.getStyleDateTimeCenterWithBorder(excel));
        // 边框 + 左边
        if (border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleDateTimeLeftWithBorder(excel));
        }
        // 无边框 + 左边
        if (!border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleDateTimeLeftNoBorder(excel));
        }
        // 无边框 + 中间
        if (!border && HorizontalAlignment.CENTER == align) {
            cell.setCellStyle(style.getStyleDateTimeCenterNoBorder(excel));
        }
    }

    /**
     * @param excel  excel
     * @param cell   cell
     * @param align  align
     * @param border border
     */
    protected static void setWrapTextStyle(Excel excel, SXSSFCell cell, HorizontalAlignment align, boolean border) {
        ExcelStyle style = excel.getStyle();
        cell.setCellStyle(style.getStyleWrapTextCenterWithBorder(excel));
        // 边框 + 左边
        if (border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleWrapTextLeftWithBorder(excel));
        }
        // 无边框 + 左边
        if (!border && HorizontalAlignment.LEFT == align) {
            cell.setCellStyle(style.getStyleWrapTextLeftNoBorder(excel));
        }
        // 无边框 + 中间
        if (!border && HorizontalAlignment.CENTER == align) {
            cell.setCellStyle(style.getStyleWrapTextCenterNoBorder(excel));
        }
    }


    public static Excel getExcel(List<OrderVo> orderList) {
        //System.out.println("数据准备：" + sdf.format(new Date()));

        Excel excel = new Excel();
        excel.setTitle("标题");
        excel.setCreateBy("虾米");
        excel.setDateFrom("2017-07-01");
        excel.setDateTo("2017-07-12");
        //excel.setSavePath(savePath);
        String[] header = {"序号", "日期", "时间", "数字", "row合并", "col合并1", "col合并2", "超长文字自动换行"};
        excel.setHeader(header);

        for (int i = 0; i < 120; i++) {

            // 多 Sheet，每 30 条数据一个Sheet 【注意，Sheet 分离时，不能有row合并，否则排版会异常】
            if (i > 1 && i % 30 == 0) {
                excel.addNewSheet();
            }
            ExcelRow row = excel.createRow();
            row.addCell(i + 1);                             // 序号
            row.addCell(new java.sql.Date(new Date().getTime()));    // 日期
            row.addCell(new Date());                                 // 时间
            row.addCell(12.1222);                        // 数字
            if (i % 3 == 0) {                                             // row合并
                row.addCell("row合并", 1, 3);
            }
            row.addCell("col合并", 2, 1);      // col合并
            //超长文字自动换行
            row.addCell("超长文字自动换行，靠左边，超长文字自动换行，靠左边，超长文字自动换行，超长文字自动换行，靠左边，超长文字自动换行，靠左边，超长文字自动换行，靠左边，超长文字自动换行，靠左边", ExcelUtil.ALIGN_LEFT);
        }
        //System.out.println("数据准备完成，准备生成excel：" + sdf.format(new Date()));
        return excel;
    }

}