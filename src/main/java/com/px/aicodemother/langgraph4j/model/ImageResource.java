package com.px.aicodemother.langgraph4j.model;

import com.px.aicodemother.langgraph4j.enums.ImageCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * packageName: com.px.aicodemother.langgraph4j.model
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImageResource
 * @date: 2025/9/26 12:01
 * @description: 图片资源
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResource implements Serializable {
    /**
     * 图片类别
     */
    private ImageCategoryEnum category;

    /**
     * 图片描述
     */
    private String description;

    /**
     * 图片地址
     */
    private String url;

    @Serial
    private static final long serialVersionUID = 1L;
}
