package com.body.aiscript.service;

import com.body.aiscript.dto.ConversionResponse;
import com.body.aiscript.dto.NovelInput;
import com.body.aiscript.model.*;
import com.body.aiscript.model.Script.RevisionEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 小说→剧本转换服务
 *
 * 核心流程：
 * 1. 接收小说文本
 * 2. 构建结构化 prompt
 * 3. 调用 DeepSeek API
 * 4. 从 AI 回复中提取 YAML
 * 5. 解析为 Script 对象并返回
 */
@Service
public class ScriptConversionService {

    private static final Logger log = LoggerFactory.getLogger(ScriptConversionService.class);

    private final DeepSeekService deepSeekService;
    private final ObjectMapper jsonMapper;
    private final ObjectMapper yamlMapper;

    public ScriptConversionService(DeepSeekService deepSeekService,
                                   @Qualifier("jsonMapper") ObjectMapper jsonMapper,
                                   @Qualifier("yamlMapper") ObjectMapper yamlMapper) {
        this.deepSeekService = deepSeekService;
        this.jsonMapper = jsonMapper;
        this.yamlMapper = yamlMapper;
    }

    /**
     * 将小说文本转换为结构化剧本
     */
    public ConversionResponse convert(NovelInput input) {
        long startTime = System.currentTimeMillis();

        try {
            // 1. 构建 Prompt
            String systemPrompt = buildSystemPrompt();
            String userMessage = buildUserMessage(input);

            // 2. 调用 AI
            String aiResponse = deepSeekService.chat(
                    systemPrompt, userMessage,
                    0.7,    // temperature: 创造性适中
                    16000   // max_tokens: 足够输出完整剧本
            );

            // 3. 从回复中提取 YAML
            String yamlContent = extractYaml(aiResponse);
            if (yamlContent == null || yamlContent.isBlank()) {
                return ConversionResponse.fail("AI 返回内容中未找到有效的 YAML 剧本");
            }

            // 4. 解析为 Script 对象
            Script script = parseScript(yamlContent);
            if (script == null) {
                return ConversionResponse.fail("YAML 剧本解析失败，请检查 AI 输出格式");
            }

            // 5. 补充元数据
            enrichMeta(script, input);

            // 6. 构建响应
            long elapsed = System.currentTimeMillis() - startTime;

            // 重新序列化为格式化的 YAML
            String formattedYaml = yamlMapper.writeValueAsString(script);

            ConversionResponse response = ConversionResponse.ok(script, formattedYaml);
            response.setInputWordCount(input.getContent().length());
            response.setOutputSceneCount(countScenes(script));
            response.setProcessingTimeMs(elapsed);
            response.setModelUsed("deepseek-chat");

            log.info("剧本转换成功: 输入{}字, 输出{}场, 耗时{}ms",
                    input.getContent().length(), countScenes(script), elapsed);

            return response;

        } catch (Exception e) {
            log.error("剧本转换失败", e);
            return ConversionResponse.fail("转换失败: " + e.getMessage());
        }
    }

    // ==================== Prompt 构建 ====================

    /**
     * 系统提示词——定义 AI 的角色和输出规范
     */
    private String buildSystemPrompt() {
        return """
            你是一位资深的剧本分析师和编剧，擅长将小说文本改编为结构化的影视剧本。

            你的任务是将用户提供的小说文本转换为标准的 YAML 格式剧本。请严格遵循以下要求：

            ## 输出格式要求
            你必须输出一个完整的 YAML 文档，结构如下：

            ```yaml
            script:
              meta:
                title: "剧本标题"
                original_title: "原著标题"
                original_author: "原著作者"
                script_version: "1.0.0"
                genre: "题材"
                tags: ["标签1", "标签2"]
                language: "zh-CN"
                source_chapters: 章节数
                word_count: 字数
                created_at: "ISO 8601 时间"
                notes: "备注"

              characters:
                - id: "char_001"
                  name: "角色名"
                  alias: ["别名"]
                  role: "protagonist|antagonist|supporting|minor|cameo"
                  archetype: "角色原型（英雄/导师/盟友/骗子等）"
                  gender: "男/女/其他"
                  age: "年龄"
                  appearance: "外貌描述"
                  personality: ["特质1", "特质2"]
                  background: "背景故事摘要"
                  motivation: "核心动机"
                  arc: "角色成长轨迹"
                  relationships:
                    - target: "char_002"
                      type: "关系类型"
                      description: "关系描述"
                  notes: "备注"

              structure:
                acts:
                  - act_number: 1
                    title: "第X幕：标题"
                    synopsis: "幕的概要"
                    scenes:
                      - scene_number: 1
                        scene_title: "场标题"
                        location: "地点描述"
                        time: "时间描述（清晨/深夜等）"
                        mood: "氛围（紧张/温馨/悲伤等）"
                        lighting: "灯光提示"
                        characters_present: ["char_001", "char_002"]
                        source_chapter: 1
                        synopsis: "本场概要"
                        content:
                          - type: "action|dialogue|transition|note"
                            character: "char_001"        # 仅 dialogue
                            character_name: "角色名"      # 仅 dialogue
                            text: "内容文本"
                            emotion: "情感"              # 仅 dialogue（可选）
                            subtext: "潜台词"            # 仅 dialogue（可选）
                            camera: "镜头指示"           # 仅 action（可选）
                            duration_hint: "时长提示"    # 可选
                          # ... 更多内容块

              revision_history:
                - version: "1.0.0"
                  date: "日期"
                  author: "AI Script Converter"
                  changes: "基于小说自动转换生成初稿"
            ```

            ## 内容块类型说明
            - **action**: 动作描写、场景描述、镜头指示——描述画面中发生的事情
            - **dialogue**: 角色对话——必须有 character 和 character_name 字段
            - **transition**: 场景切换——如"淡入淡出"、"切至"
            - **note**: 导演/编剧备注——制作提示、情绪指导、特效说明

            ## 改编原则
            1. **保留原著精髓**：保留小说的核心情节、人物关系和主题
            2. **视觉化呈现**：将小说中的心理描写、叙述性文字转化为可视化的场景、动作和对话
            3. **节奏控制**：合理划分幕和场，确保每一场有一个清晰的戏剧目的
            4. **对话提炼**：从小说叙述中提炼和重构对话，使其更适合影视表现
            5. **场景整合**：将分散的叙述整合为紧凑的场景，减少冗余
            6. **人物弧光**：确保每个主要角色有清晰的动机和成长轨迹

            ## 重要规则
            - 必须使用 YAML 格式输出，不要用 markdown 代码块包裹整个 YAML（可以包裹在 ```yaml ``` 中）
            - 角色 id 必须使用 "char_XXX" 格式（XXX 为三位数字）
            - 场景中的 characters_present 必须引用角色 id
            - 如果小说少于3章内容，尽量拆分出至少3场以上的场景
            - 所有 content 块必须包含 type 和 text 字段
            - 对于对话类型的 content 块，必须包含 character 和 character_name 字段
            """;
    }

    /**
     * 用户消息——携带具体的小说文本
     */
    private String buildUserMessage(NovelInput input) {
        StringBuilder sb = new StringBuilder();
        sb.append("请将以下小说文本转换为 YAML 格式的影视剧本。\n\n");

        sb.append("【小说信息】\n");
        sb.append("标题：").append(input.getTitle()).append("\n");
        if (input.getAuthor() != null && !input.getAuthor().isBlank()) {
            sb.append("作者：").append(input.getAuthor()).append("\n");
        }
        if (input.getGenre() != null && !input.getGenre().isBlank()) {
            sb.append("题材：").append(input.getGenre()).append("\n");
        }
        if (input.getLanguage() != null && !input.getLanguage().isBlank()) {
            sb.append("语言：").append(input.getLanguage()).append("\n");
        }
        if (input.getChapters() != null) {
            sb.append("章节数：").append(input.getChapters()).append("\n");
        }
        if (input.getStyleNotes() != null && !input.getStyleNotes().isBlank()) {
            sb.append("风格备注：").append(input.getStyleNotes()).append("\n");
        }

        sb.append("\n【小说正文】\n");
        sb.append(input.getContent());

        sb.append("\n\n请基于以上小说内容，生成完整的 YAML 格式剧本。直接输出 YAML。");
        return sb.toString();
    }

    // ==================== YAML 提取与解析 ====================

    /**
     * 从 AI 回复中提取 YAML 内容
     * AI 可能将 YAML 包裹在 markdown 代码块中，也可能直接输出
     */
    private String extractYaml(String aiResponse) {
        // 尝试匹配 ```yaml ... ``` 代码块
        Pattern p = Pattern.compile("```ya?ml\\s*\\n?(.*?)```", Pattern.DOTALL);
        Matcher m = p.matcher(aiResponse);
        if (m.find()) {
            return m.group(1).trim();
        }

        // 尝试匹配 ``` ... ``` 代码块（无语言标记）
        p = Pattern.compile("```\\s*\\n?(.*?)```", Pattern.DOTALL);
        m = p.matcher(aiResponse);
        if (m.find()) {
            String content = m.group(1).trim();
            // 验证它看起来像 YAML
            if (content.contains("script:") || content.contains("meta:")) {
                return content;
            }
        }

        // 尝试查找 "script:" 开头的内容
        int idx = aiResponse.indexOf("script:");
        if (idx >= 0) {
            // 确保从 "script:" 开始，吞掉前面的空白行
            String sub = aiResponse.substring(idx).trim();
            // 截断可能存在尾部的解释文字
            int endIdx = sub.lastIndexOf("\n");
            if (endIdx > 0) {
                String lastLine = sub.substring(endIdx).trim();
                if (!lastLine.isEmpty() && !lastLine.startsWith(" ") && !lastLine.contains(":")) {
                    sub = sub.substring(0, endIdx).trim();
                }
            }
            return sub;
        }

        return null;
    }

    /**
     * 将 YAML 字符串解析为 Script 对象
     */
    private Script parseScript(String yamlContent) {
        try {
            // Jackson YAML 解析：顶层 key 是 "script"
            JsonNode root = yamlMapper.readTree(yamlContent);

            // 支持带 "script:" 包裹和不带包裹两种格式
            JsonNode scriptNode = root.has("script") ? root.get("script") : root;

            // 使用 jsonMapper 做标准反序列化（Jackson 对 JSON 节点的类型转换比 YAML 更稳定）
            return jsonMapper.treeToValue(scriptNode, Script.class);

        } catch (Exception e) {
            log.error("YAML 解析失败: {}", e.getMessage());
            log.debug("原始 YAML:\n{}", yamlContent);
            return null;
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 补充/修正元数据
     */
    private void enrichMeta(Script script, NovelInput input) {
        ScriptMeta meta = script.getMeta();
        if (meta == null) {
            meta = new ScriptMeta();
            script.setMeta(meta);
        }
        if (meta.getTitle() == null) meta.setTitle(input.getTitle());
        if (meta.getOriginalTitle() == null) meta.setOriginalTitle(input.getTitle());
        if (meta.getOriginalAuthor() == null && input.getAuthor() != null) meta.setOriginalAuthor(input.getAuthor());
        if (meta.getScriptVersion() == null) meta.setScriptVersion("1.0.0");
        if (meta.getLanguage() == null) meta.setLanguage(input.getLanguage());
        if (meta.getCreatedAt() == null) {
            meta.setCreatedAt(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        }
        if (meta.getWordCount() == null) meta.setWordCount(input.getContent().length());

        // 补充修订历史
        if (script.getRevisionHistory() == null || script.getRevisionHistory().isEmpty()) {
            script.setRevisionHistory(List.of(
                    new RevisionEntry("1.0.0",
                            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                            "AI Script Converter",
                            "基于小说《" + input.getTitle() + "》自动转换生成初稿"
                    )
            ));
        }
    }

    /**
     * 统计场次数
     */
    private int countScenes(Script script) {
        int count = 0;
        if (script.getStructure() != null && script.getStructure().getActs() != null) {
            for (Act act : script.getStructure().getActs()) {
                if (act.getScenes() != null) {
                    count += act.getScenes().size();
                }
            }
        }
        return count;
    }
}
