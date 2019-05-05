package com.my.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressEntity implements Serializable {

    @Excel(name = "第一_first,第二_secend,第三_thread,第四_four,第五_five", orderNum = "8", needMerge = true)
    private String address;

    public AddressEntity(String address) {
        this.address = address;
    }
}
