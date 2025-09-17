package com.px.aicodemother.common;

import lombok.Data;

/**
 * packageName: com.px.aicodemother.common
 *
 * @author: idpeng
 * @version: 1.0
 * @className: PageRequest
 * @date: 2025/9/17 17:02
 * @description: 分页请求
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认：降序）
     */
    private String sortOrder = "descend";

}
