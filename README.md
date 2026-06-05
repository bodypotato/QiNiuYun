# 🎬 AI Script — 小说自动转剧本

将 3 章以上的小说文本自动转换为 **YAML 格式的结构化剧本**，利用 DeepSeek AI 进行智能改编，让作者快速获得可编辑、可打磨的剧本初稿。

## 📦 项目结构

```
├── src/main/java/              # Java 源码（Controller / Service / Model）
├── src/main/resources/
│   ├── application.yaml        # 配置（端口、API Key）→ gitignore 不提交
│   ├── application-example.yaml # 配置模板 → 复制后填入自己的 Key
│   └── static/index.html       # 前端页面（浏览器直接访问）
├── docs/script-yaml-schema.md  # YAML 剧本 Schema 设计文档
├── ../electron/                # 桌面客户端（Electron）→ 打包为 .exe
├── ../standalone/              # 独立前端 → 双击 HTML 即用，可连任意后端
└── ../.github/workflows/       # GitHub Actions → 自动构建发布 Release
```

## 🚀 快速开始

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

## 🖥 其他使用方式

| 方式 | 说明 |
|------|------|
| 🌐 浏览器访问 | 直接打开 `http://部署服务器地址:8080` |
| 📄 独立前端 | 下载 [`AIScript-standalone.zip`](../../releases)，解压双击 `index.html`，配后端地址 |
| 🖥 桌面 EXE | 下载 [`AIScript-Setup.exe`](../../releases)，双击运行，配后端地址 |
| 🔧 自建后端 | clone 本仓库，配置自己的 API Key，`./gradlew bootRun` |

## 📖 YAML 剧本 Schema

详见 [`docs/script-yaml-schema.md`](docs/script-yaml-schema.md)，包含完整 Schema 定义和设计决策说明。

## 🛠 技术栈

| 层 | 技术 |
|----|------|
| 后端 | Spring Boot 4, Java 17, Jackson YAML |
| AI | DeepSeek Chat API（OpenAI 兼容） |
| 桌面 | Electron + electron-builder |
| 前端 | 原生 HTML/CSS/JS（零依赖） |
| CI/CD | GitHub Actions |
