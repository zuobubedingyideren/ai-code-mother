package com.px.aicodemother.controller;

import com.px.aicodemother.common.BaseResponse;
import com.px.aicodemother.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName: com.px.aicodemother.controller
 *
 * @author: idpeng
 * @version: 1.0
 * @className: HealthController
 * @date: 2025/9/17 16:24
 * @description: 健康检测接口
 */
@RestController
@RequestMapping("/health")
@Tag(name = "健康检测", description = "健康检查接口")
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
