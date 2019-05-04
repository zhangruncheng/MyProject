package com.my.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

@ExcelTarget("teacherEntity")
@Data
public class TeacherEntity {
    private String id;
    /** name */
    @Excel(name = "主讲老师_major,代课老师_absent", orderNum = "1", isImportField = "true_major,true_absent",
            needMerge = true)
    private String name;

    public TeacherEntity() {
    }

    public TeacherEntity(String name) {
        this.name = name;
    }

    public TeacherEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
