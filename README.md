# AI Script — 小说自动转剧本

将 3 章以上的小说文本自动转换为 **YAML 格式的结构化剧本**，利用 DeepSeek AI 进行智能改编，让作者快速获得可编辑、可打磨的剧本初稿。

## 使用方式

| 方式 | 说明                                   | 状态 |
|------|--------------------------------------|------|
| **本地部署**（推荐） | 克隆仓库，配置自己的 DeepSeek API Key，本地启动即可使用 | 可用 |
| **桌面客户端** | 前往右侧 [Releases] 下载 `.exe`，双击即可使用     | 可用 |
| **直连地址** | 直接访问部署好的服务地址，无需配置                    | 暂时无法使用 |

> **直连地址**：`http://你的服务器地址:8080`（服务正在部署中，敬请期待）

## 代码位置

| 代码 | 路径 |
|------|------|
| **前端** | [`src/main/resources/static/`](src/main/resources/static/) — HTML/CSS/JS 页面 |
| **后端** | [`src/main/java/`](src/main/java/) — Spring Boot Controller / Service / Model |

## 项目结构

```
├── src/main/java/              # Java 源码（Controller / Service / Model）
├── src/main/resources/
│   ├── application.yaml        # 配置（端口、API Key）→ gitignore 不提交
│   ├── application-example.yaml # 配置模板 → 复制后填入自己的 Key
│   └── static/index.html       # 前端页面（浏览器直接访问）
├── docs/script-yaml-schema.md  # YAML 剧本 Schema 设计文档
└── ../.github/workflows/       # GitHub Actions → 自动构建发布 Release
```

## 快速开始

### 1. 配置 API Key

```bash
cp src/main/resources/application-example.yaml src/main/resources/application.yaml
```

编辑 `application.yaml`，将 `你的API密钥` 替换为你的 [DeepSeek API Key](https://platform.deepseek.com)。

### 2. 启动

```bash
./gradlew bootRun
```

### 3. 打开浏览器

访问 `http://localhost:8080`，粘贴小说正文即可转换。

## 直连地址

直连地址模式下，服务已部署在服务器上，用户**无需配置 API Key**，直接打开浏览器访问即可使用。

> 该功能目前**暂时无法使用**，请先使用上方的本地部署方式。开放后会在这里更新地址。

## YAML 剧本 Schema

详见 [`docs/script-yaml-schema.md`](docs/script-yaml-schema.md)，包含完整 Schema 定义和设计决策说明。

## 技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 4, Java 17, Jackson YAML |
| AI | DeepSeek Chat API（OpenAI 兼容） |
| 桌面 | Electron + electron-builder |
| 前端 | 原生 HTML/CSS/JS（零依赖） |
| CI/CD | GitHub Actions |
