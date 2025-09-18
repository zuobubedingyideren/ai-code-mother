package com.px.aicodemother.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;

/**
 * packageName: com.px.aicodemother.generator
 *
 * @author: idpeng
 * @version: 1.0
 * @className: MyBatisCodeGenerator
 * @date: 2025/9/17 22:35
 * @description: 代码生成器
 */
public class MyBatisCodeGenerator {

    // 需要生成的表名
    private static final String[] TABLE_NAMES = {"user"};

    /**
     * MyBatis代码生成器主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 获取数据源信息
        Dict dict = YamlUtil.loadByPath("application.yml");
        Map<String, Object> dataSourceConfig = dict.getByPath("spring.datasource");
        String url = String.valueOf(dataSourceConfig.get("url"));
        String username = String.valueOf(dataSourceConfig.get("username"));
        String password = String.valueOf(dataSourceConfig.get("password"));
        // 配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 创建配置内容
        GlobalConfig globalConfig = createGlobalConfig();

        // 通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        // 生成代码
        generator.generate();
    }

    /**
     * 创建MyBatis-Flex代码生成器的全局配置
     * 详细配置见：<a href="https://mybatis-flex.com/zh/others/codegen.html">...</a>
     *
     * @return GlobalConfig 全局配置对象
     */
    public static GlobalConfig createGlobalConfig() {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        // 设置根包，建议先生成到一个临时目录下，生成代码后，再移动到项目目录下
        globalConfig.getPackageConfig()
                .setBasePackage("com.px.aicodemother.genresult");

        // 设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                // 设置逻辑删除的默认字段名称
                .setLogicDeleteColumn("isDelete");

        // 设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        // 设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();

        // 设置生成 service
        globalConfig.enableService();
        globalConfig.enableServiceImpl();

        // 设置生成 controller
        globalConfig.enableController();

        // 设置生成时间和字符串为空，避免多余的代码改动
        globalConfig.getJavadocConfig()
                .setAuthor("px")
                .setSince("");
        return globalConfig;
    }
}

