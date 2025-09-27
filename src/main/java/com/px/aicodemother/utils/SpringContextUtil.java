package com.px.aicodemother.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * packageName: com.px.aicodemother.utils
 *
 * @author: idpeng
 * @version: 1.0
 * @className: SpringContextUtil
 * @date: 2025/9/27 10:03
 * @description: Spring上下文工具类 用于在静态方法中获取Spring Bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;


    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext Spring应用上下文环境
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取Spring容器中的Bean实例
     *
     * @param clazz Bean的类对象
     * @param <T>   Bean的类型
     * @return 返回指定类型的Bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据名称获取Spring容器中的Bean实例
     *
     * @param name Bean的名称
     * @return 返回指定名称的Bean实例
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 根据名称和类型获取Spring容器中的Bean实例
     *
     * @param name  Bean的名称
     * @param clazz Bean的类对象
     * @param <T>   Bean的类型
     * @return 返回指定名称和类型的Bean实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

}
