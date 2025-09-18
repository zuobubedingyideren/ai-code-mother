package com.px.aicodemother.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * packageName: com.px.aicodemother.config
 *
 * @author: idpeng
 * @version: 1.0
 * @className: JsonConfig
 * @date: 2025/9/18 10:33
 * @description: 添加 Long 转 json 精度丢失的配置
 */
@JsonComponent
public class JsonConfig {

    /**
     * 创建并配置Jackson ObjectMapper实例
     * <p>该方法主要用于解决JavaScript中Long类型精度丢失问题，将Long类型序列化为字符串</p>
     *
     * @param builder Jackson对象映射器构建器
     * @return 配置好的ObjectMapper实例
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
