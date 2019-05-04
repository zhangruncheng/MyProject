package com.my.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StudentEntity implements Serializable {

    private String        id;
    /**
     * 学生姓名
     */
    @Excel(name = "学生姓名", height = 20, width = 30, isImportField = "true_st")
    private String        name;
    /**
     * 学生性别
     */
    @Excel(name = "学生性别", replace = { "男_0", "女_1" }, suffix = "生", isImportField = "true_st")
    private int           sex;

    @Excel(name = "出生日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
    private Date          birthday;

    @Excel(name = "进校日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd")
    private Date registrationDate;

    @Excel(name = "钱财",type = 10 ,orderNum = "5", width = 20)
    private Integer money;

    public StudentEntity() {
    }

    public StudentEntity(String id, String name, int sex, Date birthday, Date registrationDate,Integer money) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.money = money;
    }

    public StudentEntity(String id, String name, int sex, Date birthday, Date registrationDate) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
    }
}
