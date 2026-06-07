# AI Script — 小说自动转剧本

# Demo视频链接：https://www.bilibili.com/video/BV1diEx6EEfM/?vd_source=6f676971d37d2da03768ffee98a9923b

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

## 第三方依赖

### 后端（Spring Boot）

构建工具：**Gradle** (`build.gradle`)

#### Gradle 插件

| 插件 | 版本 | 说明 |
|------|------|------|
| `org.springframework.boot` | `4.0.6` | Spring Boot 插件 |
| `io.spring.dependency-management` | `1.1.7` | Spring 依赖管理插件 |

#### 运行时依赖

| 依赖 | GroupId : ArtifactId | 说明 |
|------|---------------------|------|
| Spring Boot Starter | `org.springframework.boot:spring-boot-starter` | Spring Boot 核心启动器 |
| Spring Boot Web | `org.springframework.boot:spring-boot-starter-web` | Web 服务（内嵌 Tomcat） |
| Spring Boot Validation | `org.springframework.boot:spring-boot-starter-validation` | 参数校验 |
| Jackson YAML | `com.fasterxml.jackson.dataformat:jackson-dataformat-yaml` | YAML 格式解析与生成 |
| Jackson Databind | `com.fasterxml.jackson.core:jackson-databind` | JSON/对象映射 |

#### 编译期依赖

| 依赖 | GroupId : ArtifactId | 说明 |
|------|---------------------|------|
| Lombok | `org.projectlombok:lombok` | 简化 Java 代码（`@Data`、`@Slf4j` 等注解） |

#### 测试依赖

| 依赖 | GroupId : ArtifactId | 说明 |
|------|---------------------|------|
| Spring Boot Test | `org.springframework.boot:spring-boot-starter-test` | 测试框架集成 |
| JUnit Platform Launcher | `org.junit.platform:junit-platform-launcher` | JUnit 5 测试启动器 |

### 桌面客户端（Electron）

构建工具：**npm** (`electron/package.json`)

| 依赖 | 版本 | 说明 |
|------|------|------|
| `electron` | `^37.10.3` | Electron 桌面运行时 |
| `electron-builder` | `^26.0.0` | 打包为 `.exe` 等安装程序 |

### 前端

无第三方依赖，使用原生 HTML/CSS/JavaScript。

### 外部服务

| 服务 | 说明 |
|------|------|
| [DeepSeek Chat API](https://platform.deepseek.com) | AI 文本处理，OpenAI 兼容接口 |
