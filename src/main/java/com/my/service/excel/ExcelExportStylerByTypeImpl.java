package com.my.service.excel;

import cn.afterturn.easypoi.excel.entity.vo.BaseEntityTypeConstants;
import org.apache.poi.ss.usermodel.*;

/**
 * @Author : zhangruncheng
 * @Date : 2019-05-05  10:12
 * @Version : 1.0.0
 **/
public class ExcelExportStylerByTypeImpl extends AbstractExcelExportStyleByType {


    protected static final short NUMBER_FORMAT = (short) BuiltinFormats.getBuiltinFormat("#,##0");

    public ExcelExportStylerByTypeImpl(Workbook workbook){
        super.createStyles(workbook);
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook,int type, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        if (type == BaseEntityTypeConstants.DOUBLE_TYPE) {
            style.setDataFormat(NUMBER_FORMAT);
        } else {
            style.setDataFormat(STRING_FORMAT);
        }
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, int type, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFillForegroundColor((short) 41);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        if (type == BaseEntityTypeConstants.DOUBLE_TYPE) {
            style.setDataFormat(NUMBER_FORMAT);
        } else {
            style.setDataFormat(STRING_FORMAT);
        }
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    /**
     * 列表头样式
     *
     * @param headerColor
     */
    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        font.setFontHeightInPoints((short) 24);
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(headerColor);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    /**
     * 标题样式
     *
     * @param color
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setFillForegroundColor(color);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setWrapText(true);
        return titleStyle;
    }
}
