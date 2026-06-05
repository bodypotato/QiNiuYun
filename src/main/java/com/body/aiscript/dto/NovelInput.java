package com.body.aiscript.dto;

import jakarta.validation.constraints.Max;
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
    @Size(min = 100, max = 50000, message = "小说正文需在100~50000字之间")
    private String content;

    private String genre;      // 题材提示（可选）
    private String language = "zh-CN";

    @Min(value = 1, message = "章节数至少为1")
    @Max(value = 6, message = "章节数最多为6章")
    private Integer chapters;  // 章节数（可选，用于校验）

    private String styleNotes; // 风格备注（可选，如 "偏文艺片风格"）
}
