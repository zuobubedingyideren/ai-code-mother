# Mermaid Chrome 配置示例

## 配置说明

为了解决 Mermaid CLI 的 Puppeteer 和 Chromium 依赖问题，现在支持通过配置文件指定自定义的 Chrome 浏览器路径。

## 配置方式

在 `application.yml` 或 `application.properties` 中添加以下配置：

### application.yml
```yaml
mermaid:
  chrome:
    executable:
      path: "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"  # Windows 示例
      # path: "/usr/bin/google-chrome"  # Linux 示例
      # path: "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"  # macOS 示例
  puppeteer:
    timeout: 30000  # 超时时间（毫秒），默认 30 秒
```

### application.properties
```properties
# Windows 示例
mermaid.chrome.executable.path=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe
# Linux 示例
# mermaid.chrome.executable.path=/usr/bin/google-chrome
# macOS 示例
# mermaid.chrome.executable.path=/Applications/Google Chrome.app/Contents/MacOS/Google Chrome

# 超时配置
mermaid.puppeteer.timeout=30000
```

## 常见 Chrome 路径

### Windows
- `C:\Program Files\Google\Chrome\Application\chrome.exe`
- `C:\Program Files (x86)\Google\Chrome\Application\chrome.exe`
- `%LOCALAPPDATA%\Google\Chrome\Application\chrome.exe`

### Linux
- `/usr/bin/google-chrome`
- `/usr/bin/chromium-browser`
- `/opt/google/chrome/chrome`

### macOS
- `/Applications/Google Chrome.app/Contents/MacOS/Google Chrome`
- `/Applications/Chromium.app/Contents/MacOS/Chromium`

## 功能特性

1. **自动验证路径**：系统会自动验证配置的 Chrome 路径是否有效
2. **优雅降级**：如果配置的路径无效，会自动回退到 Puppeteer 默认的 Chromium
3. **详细日志**：提供详细的日志信息，便于问题排查
4. **跨平台支持**：自动适配不同操作系统的启动参数

## 生成的 Puppeteer 配置文件示例

```json
{
  "executablePath": "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
  "timeout": 30000,
  "args": [
    "--no-sandbox",
    "--disable-setuid-sandbox",
    "--disable-dev-shm-usage",
    "--disable-gpu"
  ]
}
```

## 使用建议

1. 如果系统已安装 Chrome 浏览器，建议配置 `mermaid.chrome.executable.path`
2. 如果使用 Docker 环境，可以安装 `chrome-headless-shell` 并配置相应路径
3. 对于 CI/CD 环境，建议使用环境变量来配置路径

## 故障排除

如果仍然遇到问题，请检查：
1. Chrome 路径是否正确
2. Chrome 是否有执行权限
3. 系统是否缺少必要的依赖库（Linux 环境）
4. 防火墙或安全软件是否阻止了 Chrome 启动