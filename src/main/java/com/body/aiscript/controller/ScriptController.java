package com.body.aiscript.controller;

import com.body.aiscript.dto.ConversionResponse;
import com.body.aiscript.dto.NovelInput;
import com.body.aiscript.dto.RewriteRequest;
import com.body.aiscript.dto.RewriteResponse;
import com.body.aiscript.model.Script;
import com.body.aiscript.service.ScriptConversionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 剧本转换 REST 控制器
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // 允许前端跨域访问
public class ScriptController {

    private static final Logger log = LoggerFactory.getLogger(ScriptController.class);

    /** 单次转换最大字数 */
    private static final int MAX_WORD_COUNT = 50_000;
    /** 最大章节数 */
    private static final int MAX_CHAPTERS = 6;

    private final ScriptConversionService conversionService;
    private final ObjectMapper yamlMapper;

    public ScriptController(ScriptConversionService conversionService,
                            @Qualifier("yamlMapper") ObjectMapper yamlMapper) {
        this.conversionService = conversionService;
        this.yamlMapper = yamlMapper;
    }

    /**
     * 将小说转换为剧本
     *
     * POST /api/convert
     * Content-Type: application/json
     *
     * 请求体示例：
     * {
     *   "title": "剑雨江湖",
     *   "author": "江湖客",
     *   "content": "第一章 下山...（小说正文）",
     *   "genre": "武侠",
     *   "chapters": 5,
     *   "styleNotes": "参考王家卫电影风格"
     * }
     */
    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertNovel(@RequestBody NovelInput input) {
        // 自动提取标题（与文件上传端点保持一致）
        if (input.getTitle() == null || input.getTitle().isBlank()) {
            input.setTitle(extractTitle(input.getContent(), null));
        }
        if (input.getAuthor() == null || input.getAuthor().isBlank()) {
            input.setAuthor(extractAuthor(input.getContent()));
        }

        // 手动校验必填字段
        if (input.getContent() == null || input.getContent().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ConversionResponse.fail("小说正文不能为空"));
        }
        if (input.getContent().length() < 100) {
            return ResponseEntity.badRequest()
                    .body(ConversionResponse.fail("小说正文需在100~50000字之间"));
        }
        if (input.getContent().length() > MAX_WORD_COUNT) {
            return ResponseEntity.badRequest()
                    .body(ConversionResponse.fail(String.format("字数超限：当前 %d 字，最多允许 %d 字",
                            input.getContent().length(), MAX_WORD_COUNT)));
        }

        log.info("收到转换请求: title={}, contentLength={}, chapters={}",
                input.getTitle(), input.getContent().length(), input.getChapters());

        ConversionResponse response = conversionService.convert(input);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    /**
     * 将小说转换为纯 YAML 格式剧本（直接返回 YAML 文本）
     *
     * POST /api/convert/yaml
     * Accept: application/x-yaml
     */
    @PostMapping(value = "/convert/yaml", produces = "application/x-yaml;charset=UTF-8")
    public ResponseEntity<String> convertNovelToYaml(@RequestBody NovelInput input) {
        // 自动提取标题（与文件上传端点保持一致）
        if (input.getTitle() == null || input.getTitle().isBlank()) {
            input.setTitle(extractTitle(input.getContent(), null));
        }
        if (input.getAuthor() == null || input.getAuthor().isBlank()) {
            input.setAuthor(extractAuthor(input.getContent()));
        }

        // 手动校验必填字段
        if (input.getContent() == null || input.getContent().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("# 小说正文不能为空");
        }
        if (input.getContent().length() < 100) {
            return ResponseEntity.badRequest()
                    .body("# 小说正文需在100~50000字之间");
        }
        if (input.getContent().length() > MAX_WORD_COUNT) {
            return ResponseEntity.badRequest()
                    .body(String.format("# 字数超限：当前 %d 字，最多允许 %d 字",
                            input.getContent().length(), MAX_WORD_COUNT));
        }

        log.info("收到 YAML 转换请求: title={}", input.getTitle());

        ConversionResponse response = conversionService.convert(input);

        if (response.isSuccess() && response.getRawYaml() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/x-yaml;charset=UTF-8"))
                    .body(response.getRawYaml());
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("# 转换失败\n# " + response.getMessage());
        }
    }

    /**
     * 上传 TXT 文件，将小说转换为剧本
     *
     * POST /api/convert/file
     * Content-Type: multipart/form-data
     *
     * 表单字段：
     * - file:     小说 .txt 文件（必填）
     * - title:    小说标题（选填，不填则自动从文件内容提取）
     * - author:   小说作者（选填）
     * - genre:    题材（选填）
     * - styleNotes: 风格备注（选填）
     */
    @PostMapping(value = "/convert/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConversionResponse> convertFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "styleNotes", required = false) String styleNotes) {

        // 1. 校验文件
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ConversionResponse.fail("文件为空，请上传有效的 .txt 文件"));
        }

        String originalFilename = file.getOriginalFilename();
        log.info("收到文件上传: name={}, size={}", originalFilename, file.getSize());

        // 2. 读取文件内容
        String content;
        try {
            content = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ConversionResponse.fail("读取文件失败: " + e.getMessage()));
        }

        if (content.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ConversionResponse.fail("文件内容为空"));
        }

        // 2.5 校验字数 & 章节数
        String limitError = validateLimits(content);
        if (limitError != null) {
            return ResponseEntity.badRequest().body(ConversionResponse.fail(limitError));
        }

        // 3. 自动提取元信息
        if (title == null || title.isBlank()) {
            title = extractTitle(content, originalFilename);
        }
        if (author == null || author.isBlank()) {
            author = extractAuthor(content);
        }
        int chapters = countChapters(content);

        log.info("自动识别: title={}, author={}, chapters={}", title, author, chapters);

        // 4. 构建请求并转换
        NovelInput input = new NovelInput();
        input.setTitle(title);
        input.setAuthor(author);
        input.setContent(content);
        input.setGenre(genre);
        input.setChapters(chapters > 0 ? chapters : null);
        input.setStyleNotes(styleNotes);

        ConversionResponse response = conversionService.convert(input);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    /**
     * 上传 TXT 文件，直接返回 YAML
     *
     * POST /api/convert/file/yaml
     * Content-Type: multipart/form-data
     */
    @PostMapping(value = "/convert/file/yaml",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "application/x-yaml;charset=UTF-8")
    public ResponseEntity<String> convertFileToYaml(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "styleNotes", required = false) String styleNotes) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("# 文件为空");
        }

        String content;
        try {
            content = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("# 读取文件失败: " + e.getMessage());
        }

        // 校验字数 & 章节数
        String limitError = validateLimits(content);
        if (limitError != null) {
            return ResponseEntity.badRequest().body("# " + limitError);
        }

        if (title == null || title.isBlank()) {
            title = extractTitle(content, file.getOriginalFilename());
        }
        if (author == null || author.isBlank()) {
            author = extractAuthor(content);
        }
        int chapters = countChapters(content);

        NovelInput input = new NovelInput();
        input.setTitle(title);
        input.setAuthor(author);
        input.setContent(content);
        input.setGenre(genre);
        input.setChapters(chapters > 0 ? chapters : null);
        input.setStyleNotes(styleNotes);

        ConversionResponse response = conversionService.convert(input);

        if (response.isSuccess() && response.getRawYaml() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/x-yaml;charset=UTF-8"))
                    .body(response.getRawYaml());
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("# 转换失败\n# " + response.getMessage());
        }
    }

    // ==================== TXT 元信息自动提取 ====================

    /**
     * 从文件内容中自动提取标题。
     * 优先级：书名号中的文本 > 第一行非空内容 > 文件名（去扩展名）
     */
    private String extractTitle(String content, String filename) {
        // 尝试匹配 《...》
        Pattern p = Pattern.compile("《(.+?)》");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(1);
        }
        // 尝试匹配 "标题：XXX" 或 "书名：XXX"
        p = Pattern.compile("(?:标题|书名)[：:]\s*(.+)");
        m = p.matcher(content);
        if (m.find()) {
            return m.group(1).trim();
        }
        // 用第一行非空内容（限制长度）
        String firstLine = content.lines()
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .findFirst()
                .orElse("");
        if (firstLine.length() > 2 && firstLine.length() < 80) {
            return firstLine;
        }
        // 回退到文件名
        if (filename != null && filename.contains(".")) {
            return filename.substring(0, filename.lastIndexOf('.'));
        }
        return filename != null ? filename : "未命名小说";
    }

    /**
     * 尝试从文件内容中提取作者名
     */
    private String extractAuthor(String content) {
        Pattern p = Pattern.compile("(?:作者|著)[：:]\s*(.+?)(?:\n|$)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    /**
     * 校验字数上限和章节数上限。返回 null 表示通过，否则返回错误消息。
     */
    private String validateLimits(String content) {
        int wordCount = content.length();
        if (wordCount > MAX_WORD_COUNT) {
            return String.format("字数超限：当前 %d 字，最多允许 %d 字（约 %.1f 万字）",
                    wordCount, MAX_WORD_COUNT, MAX_WORD_COUNT / 10000.0);
        }
        if (wordCount < 100) {
            return String.format("字数不足：当前仅 %d 字，至少需要 100 字（建议3章以上）", wordCount);
        }
        int chapters = countChapters(content);
        if (chapters > MAX_CHAPTERS) {
            return String.format("章节数超限：检测到 %d 章，最多允许 %d 章", chapters, MAX_CHAPTERS);
        }
        return null;
    }

    /**
     * 通过匹配 "第X章" 来统计章节数
     */
    private int countChapters(String content) {
        Pattern p = Pattern.compile("第[零一二三四五六七八九十百千0-9]+章");
        Matcher m = p.matcher(content);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * 将前端编辑后的 Script JSON 序列化为 YAML 文本
     *
     * POST /api/script/serialize
     * Content-Type: application/json
     */
    @PostMapping(value = "/script/serialize", produces = "application/x-yaml;charset=UTF-8")
    public ResponseEntity<String> serializeScript(@RequestBody Script script) {
        try {
            String yaml = yamlMapper.writeValueAsString(script);
            return ResponseEntity.ok(yaml);
        } catch (Exception e) {
            log.error("YAML 序列化失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("# 序列化失败: " + e.getMessage());
        }
    }

    /**
     * AI 辅助改写剧本片段
     *
     * POST /api/rewrite
     */
    @PostMapping("/rewrite")
    public ResponseEntity<RewriteResponse> rewrite(@RequestBody RewriteRequest request) {
        if (request.getOriginalText() == null || request.getOriginalText().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(RewriteResponse.fail("原文不能为空"));
        }
        if (request.getInstruction() == null || request.getInstruction().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(RewriteResponse.fail("改写指令不能为空"));
        }

        log.info("收到改写请求: type={}, instruction={}", request.getBlockType(), request.getInstruction());

        RewriteResponse response = conversionService.rewrite(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "AI Script Converter",
                "version", "1.0.0"
        ));
    }
}
