package com.px.aicodemother.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

/**
 * packageName: com.px.aicodemother.utils
 *
 * @author: idpeng
 * @version: 1.0
 * @className: CacheKeyUtils
 * @date: 2025/10/3 16:57
 * @description: 缓存键值工具类
 */
public class CacheKeyUtils {
    /**
     * 根据对象生成缓存键值
     * 
     * @param object 用于生成键值的对象，可以为null
     * @return 返回对象的MD5哈希值作为缓存键值
     */
    public static String generateKey(Object object) {
        if (object == null) {
            return DigestUtil.md5Hex("null");
        }
        // 将对象转换为JSON字符串, 并计算其MD5哈希值作为缓存键值
        String jsonStr = JSONUtil.toJsonStr(object);
        return DigestUtil.md5Hex(jsonStr);
    }
}