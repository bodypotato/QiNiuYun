package com.body.aiscript.controller;

import com.body.aiscript.dto.ConversionResponse;
import com.body.aiscript.dto.NovelInput;
import com.body.aiscript.service.ScriptConversionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 剧本转换 REST 控制器
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // 允许前端跨域访问
public class ScriptController {

    private static final Logger log = LoggerFactory.getLogger(ScriptController.class);

    private final ScriptConversionService conversionService;

    public ScriptController(ScriptConversionService conversionService) {
        this.conversionService = conversionService;
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
    public ResponseEntity<ConversionResponse> convertNovel(@Valid @RequestBody NovelInput input) {
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
    public ResponseEntity<String> convertNovelToYaml(@Valid @RequestBody NovelInput input) {
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
