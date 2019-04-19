package com.my.aspect.model;

import lombok.Data;

import java.util.Map;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  08:53
 * @Version : 1.0.0
 **/
@Data
public class UserDataPermission {

    private String idCard;

    private Map<String,DataPermissionCollection> dataPermissionCollectionMap;
}
