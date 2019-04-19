package com.my.bean;

import com.my.util.check.NetWork;
import com.my.util.check.Privacy;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  10:20
 * @Version : 1.0.0
 **/

@Data
public class Person implements Serializable {

    @NotBlank(message = "姓名不能为空",groups = NetWork.class)
    private String name;

    @NotBlank(message = "不能为空",groups = Privacy.class)
    private String sex;

    private Integer age;

    @NotBlank(message = "升高为必填项")
    private String hight;

    @NotBlank(message = "身份证号码不能为空",groups = NetWork.class)
    private String idCard;

    private String fatherName;

    private String mohterName;

    private String birthday;

    @NotBlank(message = "必须填写",groups ={NetWork.class,Privacy.class})
    private String address;

    private String country;
}
