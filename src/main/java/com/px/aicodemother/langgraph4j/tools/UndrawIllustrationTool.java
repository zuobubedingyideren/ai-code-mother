package com.px.aicodemother.langgraph4j.tools;

import cn.hutool.core.util.StrUtil;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.tools
 *
 * @author: idpeng
 * @version: 1.0
 * @className: UndrawIllustrationTool
 * @date: 2025/9/26 16:18
 * @description: 图片收集工具（插画图片）
 */
@Slf4j
@Component
public class UndrawIllustrationTool {

    private static final String UNDRAW_API_URL = "https://undraw.co/_next/data/Qjh7iT41P1b8amtxJWFa_/search/%s.json?term=%s";

    /**
     * 搜索插画图片，用于网站美化和装饰
     * @param query 搜索关键词
     * @return 插画图片资源列表
     */
    @Tool("搜索插画图片，用于网站美化和装饰")
    public List<ImageResource> searchIllustrations(@P("搜索关键词") String query) {
        // 初始化返回结果列表
        List<ImageResource> imageList = new ArrayList<>();

        int searchCount = 12;
        // 构造API请求URL
        String apiUrl = String.format(UNDRAW_API_URL, query, query);

        try(HttpResponse response = HttpRequest.get(apiUrl).timeout(10000).execute()) {
            // 检查HTTP响应是否成功
            if (!response.isOk()) {
                return imageList;
            }
            // 解析响应JSON数据
            JSONObject result = JSONUtil.parseObj(response.body());
            JSONObject pageProps = result.getJSONObject("pageProps");
            if (pageProps == null) {
                return imageList;
            }
            JSONArray initialResults = pageProps.getJSONArray("initialResults");
            if (initialResults == null || initialResults.isEmpty()) {
                return imageList;
            }
            // 限制返回结果数量
            int actualCount = Math.min(searchCount, initialResults.size());
            for (int i = 0; i < actualCount; i++) {
                JSONObject illustration = initialResults.getJSONObject(i);
                String title = illustration.getStr("title", "插画");
                String media = illustration.getStr("media", "");
                // 只添加有媒体链接的插画
                if (StrUtil.isNotBlank(media)) {
                    imageList.add(ImageResource.builder()
                            .category(ImageCategoryEnum.ILLUSTRATION)
                            .description(title)
                            .url(media)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("搜索插画失败：{}", e.getMessage(), e);
        }
        return imageList;
    }

}
