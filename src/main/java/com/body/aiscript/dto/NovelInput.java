package com.body.aiscript.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 小说转换请求
 */
@Data
public class NovelInput {

    @NotBlank(message = "小说标题不能为空")
    private String title;

    private String author;

    @NotBlank(message = "小说正文不能为空")
    @Size(min = 100, message = "小说正文至少需要100个字符（建议提供3个章节以上的内容）")
    private String content;

    private String genre;      // 题材提示（可选）
    private String language = "zh-CN";

    @Min(value = 1, message = "章节数至少为1")
    private Integer chapters;  // 章节数（可选，用于校验）

    private String styleNotes; // 风格备注（可选，如 "偏文艺片风格"）
}
