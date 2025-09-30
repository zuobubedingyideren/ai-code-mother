package com.px.aicodemother.langgraph4j.node.concurrent;

import com.px.aicodemother.langgraph4j.model.ImageResource;
import com.px.aicodemother.langgraph4j.state.WorkflowContext;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName: com.px.aicodemother.langgraph4j.node.concurrent
 *
 * @author: idpeng
 * @version: 1.0
 * @className: ImageAggregatorNode
 * @date: 2025/9/30 11:25
 * @description: 图片聚合节点，用于汇总各种类型的图片资源
 */
@Slf4j
public class ImageAggregatorNode {

    /**
     * 创建图片聚合节点的异步操作
     * 
     * @return AsyncNodeAction<MessagesState<String>> 返回一个异步节点操作，用于聚合不同类型图片
     */
    public static AsyncNodeAction<MessagesState<String>> create() {
        return AsyncNodeAction.node_async(state ->  {
            WorkflowContext context = WorkflowContext.getContext(state);
            List<ImageResource> allImages = new ArrayList<>();
            log.info("开始汇总图片");
            if (context.getContentImages() != null) {
                allImages.addAll(context.getContentImages());
            }
            if (context.getIllustrations() != null) {
                allImages.addAll(context.getIllustrations());
            }
            if (context.getDiagrams() != null) {
                allImages.addAll(context.getDiagrams());
            }
            if (context.getLogos() != null) {
                allImages.addAll(context.getLogos());
            }
            log.info("图片汇总完成，共收集到 {} 张图片", allImages.size());
            context.setImageList(allImages);
            context.setCurrentStep("图片聚合");
            return WorkflowContext.saveContext(context);
        });
    }
}