package com.my.aspect.context;

import com.my.aspect.model.UserDataPermission;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  08:59
 * @Version : 1.0.0
 **/
public class UserContext {

    private static ThreadLocal<UserDataPermission> dataPermissionThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<String> idCardThreadLocal = new ThreadLocal<>();

    public static void setUserDataPermission(UserDataPermission userDataPermission){
        dataPermissionThreadLocal.set(userDataPermission);
    }

    public static UserDataPermission getUserDataPermission(){
        return dataPermissionThreadLocal.get();
    }

    public static void removeUserDataPermisson(){
        dataPermissionThreadLocal.remove();
    }

    public static void setIdCard(String idCard){
        idCardThreadLocal.set(idCard);
    }
}
