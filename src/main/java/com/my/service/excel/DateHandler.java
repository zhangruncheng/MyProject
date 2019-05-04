package com.my.service.excel;

import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import com.my.bean.excel.StudentEntity;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.Map;

public class DateHandler implements IExcelDataHandler<StudentEntity> {

    /**
     * 需要处理的字段
     */
    private String[] needHandlerFields;

    @Override
    public Object exportHandler(StudentEntity obj, String name, Object value) {
        if ("钱财".equals(name)){
            String[] split = value.toString().split(",");
            StringBuilder sb = new StringBuilder();
            for (String s : split) {
                sb.append(s);
            }
            return Double.parseDouble(sb.toString());
        }
        return value;
    }

    @Override
    public String[] getNeedHandlerFields() {
        return needHandlerFields;
    }

    @Override
    public Object importHandler(StudentEntity obj, String name, Object value) {
        return null;
    }

    @Override
    public void setNeedHandlerFields(String[] fields) {
        this.needHandlerFields = fields;
    }

    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        map.put(originKey, value);
    }

    @Override
    public Hyperlink getHyperlink(CreationHelper creationHelper, StudentEntity obj, String name, Object value) {
        return null;
    }
}
