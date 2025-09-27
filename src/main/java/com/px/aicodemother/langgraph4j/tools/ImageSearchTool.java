package com.px.aicodemother.langgraph4j.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.px.aicodemother.langgraph4j.enums.ImageCategoryEnum;
import com.px.aicodemother.langgraph4j.model.ImageResource;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImageSearchTool
 * @date: 2025/9/26 15:36
 * @description: 图片搜索工具（根据关键词搜索内容图片）
 */
@Slf4j
@Component
public class ImageSearchTool {

    private static final String PEXELS_API_URL = "https://api.pexels.com/v1/search";


    @Value("${pexels.api-key}")
    private String pexelsApiKey;

    /*
     * 根据关键词搜索内容相关的图片资源
     * @param query 搜索关键词
     * @return 图片资源列表
     */
    @Tool("搜索内容相关的图片，用于网站内容展示")
    public List<ImageResource> searchContentImages(@P("搜索关键词") String query) {
        ArrayList<ImageResource> imageList = new ArrayList<>();
        int searchCount = 12;
        // 调用Pexels API搜索图片
        try(HttpResponse response = HttpRequest.get(PEXELS_API_URL)
                .header("Authorization", pexelsApiKey)
                .form("query", query)
                .form("per_page", searchCount)
                .execute()) {
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                JSONArray photos = result.getJSONArray("photos");
                // 解析API返回的图片数据
                for (int i = 0; i < photos.size(); i++) {
                    JSONObject photo = photos.getJSONObject(i);
                    JSONObject src = photo.getJSONObject("src");
                    imageList.add(ImageResource.builder()
                            .category(ImageCategoryEnum.CONTENT)
                            .description(photo.getStr("alt", query))
                            .url(src.getStr("medium"))
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("Pexels API 调用失败: {}", e.getMessage(), e);
        }
        return imageList;
    }
}
