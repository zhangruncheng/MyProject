package com.my.service.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.entity.vo.BaseEntityTypeConstants;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @Author : zhangruncheng
 * @Date : 2019-05-05  10:05
 * @Version : 1.0.0
 **/
public abstract class AbstractExcelExportStyleByType implements IExcelExportStyler {


    //单行
    protected CellStyle stringNoneStyle;
    protected CellStyle numNoneStyle;
    protected CellStyle stringNoneWrapStyle;
    protected CellStyle numNoneWrapStyle;
    //间隔行
    protected CellStyle stringSeptailStyle;
    protected CellStyle numSeptailStyle;
    protected CellStyle stringSeptailWrapStyle;
    protected CellStyle numSeptailWrapStyle;

    protected Workbook workbook;

    protected static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");

    protected void createStyles(Workbook workbook) {
        this.stringNoneStyle = stringNoneStyle(workbook, BaseEntityTypeConstants.STRING_TYPE,false);
        this.numNoneStyle = stringNoneStyle(workbook, BaseEntityTypeConstants.DOUBLE_TYPE,false);
        this.stringNoneWrapStyle = stringNoneStyle(workbook, BaseEntityTypeConstants.STRING_TYPE,true);
        this.numNoneWrapStyle = stringNoneStyle(workbook, BaseEntityTypeConstants.DOUBLE_TYPE,true);
        this.stringSeptailStyle = stringSeptailStyle(workbook, BaseEntityTypeConstants.STRING_TYPE,false);
        this.numSeptailStyle = stringSeptailStyle(workbook, BaseEntityTypeConstants.DOUBLE_TYPE,false);
        this.stringSeptailWrapStyle = stringSeptailStyle(workbook, BaseEntityTypeConstants.STRING_TYPE,true);
        this.numSeptailWrapStyle = stringSeptailStyle(workbook, BaseEntityTypeConstants.DOUBLE_TYPE,true);
        this.workbook = workbook;
    }



    /**
     * 获取样式方法
     *
     * @param noneStyler
     * @param entity
     */
    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        if (noneStyler && (entity == null || entity.isWrap())) {
            if (null != entity && entity.getType() == BaseEntityTypeConstants.DOUBLE_TYPE) {
                return numNoneWrapStyle;
            } else {
                return stringNoneWrapStyle;
            }
        }
        if (noneStyler) {
            if (null != entity && entity.getType() == BaseEntityTypeConstants.DOUBLE_TYPE) {
                return numNoneStyle;
            } else {
                return stringNoneStyle;
            }
        }

        if (noneStyler == false && (entity == null || entity.isWrap())) {
            if (null != entity && entity.getType() == BaseEntityTypeConstants.DOUBLE_TYPE) {
                return numSeptailWrapStyle;
            } else {
                return stringSeptailWrapStyle;
            }
        }
        if (noneStyler == false){
            if (null != entity && entity.getType() == BaseEntityTypeConstants.DOUBLE_TYPE) {
                return numSeptailStyle;
            } else {
                return stringSeptailStyle;
            }
        }
        return stringNoneStyle;
    }



    public abstract CellStyle stringNoneStyle(Workbook workbook, int type, boolean isWarp);

    public abstract CellStyle stringSeptailStyle(Workbook workbook, int type,  boolean isWarp);

    /**
     * 获取样式方法
     *
     * @param cell
     * @param dataRow 数据行
     * @param entity
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(dataRow % 2 == 1, entity);
    }

    /**
     * 模板使用的样式设置
     *
     * @param isSingle
     * @param excelForEachParams
     */
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }
}
