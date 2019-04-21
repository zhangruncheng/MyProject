package com.my.common;

import lombok.Data;

import java.util.List;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-20  09:51
 * @Version : 1.0.0
 **/
@Data
public class TreeNode<T> {

    /** 父id */
    private String pid;
    /** id */
    private String id;
    /** 子类 */
    private List<TreeNode<T>> childrenList;

    private T data;
}
