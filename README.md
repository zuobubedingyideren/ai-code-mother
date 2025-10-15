# AI代码梦工厂 🚀

> 一句话轻松创建网站应用的AI代码生成平台

## 📖 项目简介

AI代码梦工厂是一个基于AI技术的智能代码生成平台，用户只需要输入一句话描述，就能自动生成完整的网站应用。平台支持多种代码生成类型，提供从创建到部署的一站式服务。

## ✨ 核心功能

### 🎯 AI代码生成
- **智能理解**：基于LangChain4j和DeepSeek AI模型，理解用户需求
- **多类型支持**：支持Vue项目、静态网站等多种代码生成类型
- **实时对话**：流式响应，实时查看代码生成过程
- **智能路由**：根据任务复杂度自动选择合适的AI模型

### 🛠️ 应用管理
- **应用创建**：一键创建个人应用
- **代码预览**：实时预览生成的代码和效果
- **应用部署**：自动部署到服务器，生成访问链接
- **截图生成**：自动生成应用预览截图

### 👥 用户系统
- **用户注册/登录**：完整的用户认证体系
- **权限管理**：用户/管理员角色权限控制
- **个人中心**：管理个人应用和设置

### 📊 管理后台
- **用户管理**：管理平台用户信息
- **应用管理**：管理所有应用，支持精选推荐
- **对话管理**：查看和管理用户对话记录

## 🏗️ 技术架构

### 后端架构（微服务）

```
ai-code-mother-microservice/
├── ai-code-common/          # 公共模块
├── ai-code-model/           # 数据模型
├── ai-code-client/          # 客户端接口
├── ai-code-user/            # 用户服务 (端口: 8124)
├── ai-code-app/             # 应用服务 (端口: 8125)
├── ai-code-ai/              # AI服务
└── ai-code-screenshot/      # 截图服务 (端口: 8127)
```

#### 技术栈
- **框架**：Spring Boot 3.5.5 + Java 21
- **微服务**：Apache Dubbo 3.3.0 + Nacos注册中心
- **数据库**：MySQL + MyBatis-Flex ORM
- **缓存**：Redis + Caffeine本地缓存
- **AI集成**：LangChain4j + DeepSeek API
- **文档**：Knife4j (Swagger)
- **工具库**：Hutool、Lombok

#### 服务说明
- **ai-code-user**：用户认证、权限管理
- **ai-code-app**：应用CRUD、代码生成、部署管理
- **ai-code-ai**：AI模型调用、智能路由
- **ai-code-screenshot**：应用截图生成（基于Selenium）

### 前端架构

#### 技术栈
- **框架**：Vue 3 + TypeScript
- **构建工具**：Vite 7.0
- **UI组件**：Ant Design Vue 4.2.6
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **HTTP客户端**：Axios
- **代码高亮**：highlight.js
- **Markdown渲染**：markdown-it

#### 页面结构
```
src/
├── pages/
│   ├── HomePage.vue              # 首页
│   ├── app/
│   │   ├── AppChatPage.vue       # 应用对话页
│   │   └── AppEditPage.vue       # 应用编辑页
│   ├── user/
│   │   ├── UserLoginPage.vue     # 用户登录
│   │   └── UserRegisterPage.vue  # 用户注册
│   └── admin/
│       ├── UserManagePage.vue    # 用户管理
│       ├── AppManagePage.vue     # 应用管理
│       └── ChatManagePage.vue    # 对话管理
├── components/                   # 公共组件
├── stores/                       # 状态管理
├── api/                          # API接口
└── utils/                        # 工具函数
```

### 数据库设计

#### 核心表结构
- **user**：用户信息表
- **app**：应用信息表
- **chat_history**：对话历史表

### 中间件配置

#### Redis配置
- **用途**：Session存储、缓存、AI对话记忆
- **配置**：无密码模式，支持远程访问
- **端口**：6379

#### Nacos配置
- **用途**：微服务注册中心
- **地址**：127.0.0.1:8848
- **认证**：nacos/nacos

## 🚀 快速开始

### 环境要求

- **Java**：21+
- **Node.js**：18+
- **MySQL**：8.0+
- **Redis**：6.0+
- **Nacos**：2.0+

### 后端启动

1. **数据库初始化**
```bash
# 创建数据库并执行建表脚本
mysql -u root -p < sql/create_table.sql
```

2. **启动Redis**
```bash
# 使用Docker启动Redis
docker run -d --name redis -p 6379:6379 -v ./redis/redis.conf:/etc/redis/redis.conf redis:latest redis-server /etc/redis/redis.conf
```

3. **启动Nacos**
```bash
# 下载并启动Nacos
# 访问 http://localhost:8848/nacos
```

4. **配置AI API**
```yaml
# 修改 ai-code-app/src/main/resources/application.yml
langchain4j:
  open-ai:
    chat-model:
      api-key: <Your DeepSeek API Key>
```

5. **启动微服务**
```bash
# 进入微服务目录
cd ai-code-mother-microservice

# 编译项目
mvn clean install

# 启动各个服务
# 1. 启动用户服务
cd ai-code-user && mvn spring-boot:run

# 2. 启动应用服务
cd ai-code-app && mvn spring-boot:run

# 3. 启动截图服务
cd ai-code-screenshot && mvn spring-boot:run
```

### 前端启动

```bash
# 进入前端目录
cd yu-ai-code-mother-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问 http://localhost:5173
```

## 🔧 配置说明

### 环境变量配置

#### 前端环境变量
```bash
# .env.development
VITE_API_BASE_URL=http://localhost:8125/api
VITE_DEPLOY_DOMAIN=http://localhost

# .env.production
VITE_API_BASE_URL=https://your-api-domain.com/api
VITE_DEPLOY_DOMAIN=https://your-deploy-domain.com
```

#### 后端配置文件
```yaml
# application.yml 主要配置项
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_code_mother
    username: root
    password: root
  data:
    redis:
      host: localhost
      port: 6379

langchain4j:
  open-ai:
    chat-model:
      base-url: https://api.deepseek.com
      api-key: <Your API Key>
      model-name: deepseek-chat

dubbo:
  registry:
    address: nacos://127.0.0.1:8848?username=nacos&password=nacos
```

## 📋 API文档

启动后端服务后，可通过以下地址访问API文档：

- **用户服务**：http://localhost:8124/api/doc.html
- **应用服务**：http://localhost:8125/api/doc.html
- **截图服务**：http://localhost:8127/api/doc.html

## 🎯 核心流程

### 1. 应用创建流程
```
用户输入描述 → AI理解需求 → 选择代码生成类型 → 生成代码 → 保存应用
```

### 2. 代码生成流程
```
用户发送消息 → 智能路由选择模型 → AI生成代码 → 流式返回结果 → 保存对话历史
```

### 3. 应用部署流程
```
选择应用 → 检查代码完整性 → 构建项目 → 部署到服务器 → 生成访问链接 → 截图预览
```

## 🔒 安全特性

- **用户认证**：基于Session的用户认证
- **权限控制**：用户/管理员角色权限分离
- **参数校验**：完整的请求参数校验
- **SQL注入防护**：使用MyBatis-Flex预编译语句
- **XSS防护**：前端输入过滤和转义

## 📈 性能优化

- **缓存策略**：Redis + Caffeine多级缓存
- **连接池**：HikariCP数据库连接池
- **异步处理**：截图生成异步处理
- **流式响应**：AI对话流式返回，提升用户体验
- **CDN加速**：静态资源CDN分发

## 🛠️ 开发工具

- **IDE**：IntelliJ IDEA / VS Code
- **API测试**：Postman / Apifox
- **数据库管理**：Navicat / DataGrip
- **版本控制**：Git
- **容器化**：Docker

## 📝 更新日志

### v1.0.0 (2024-12-XX)
- ✅ 完成基础架构搭建
- ✅ 实现AI代码生成功能
- ✅ 完成用户系统
- ✅ 实现应用管理功能
- ✅ 完成部署系统
- ✅ 实现截图生成功能

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request



## 🙏 致谢

- [LangChain4j](https://github.com/langchain4j/langchain4j) - AI集成框架
- [Spring Boot](https://spring.io/projects/spring-boot) - 后端框架
- [Vue.js](https://vuejs.org/) - 前端框架
- [Ant Design Vue](https://antdv.com/) - UI组件库
- [DeepSeek](https://www.deepseek.com/) - AI模型提供商

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

---

⭐ 如果这个项目对你有帮助，请给它一个星标！