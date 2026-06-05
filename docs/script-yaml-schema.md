# 剧本 YAML Schema 设计文档

## 概述

本 Schema 定义了一种将小说文本结构化表达为剧本的 YAML 格式。设计目标是为作者提供一个**可编辑、可扩展、人机皆可读**的剧本中间格式——既保留小说的文学性，又具备剧本的可执行性。

## 设计原则

### 1. 分层结构，逐步细化
剧本采用 **元数据 → 角色 → 结构(幕→场→内容)** 的四层递进结构。每一层解决不同粒度的创作问题：
- 元数据层：回答"这是什么故事"
- 角色层：回答"谁在故事中"
- 结构层：回答"故事如何组织"
- 内容层：回答"每一刻发生了什么"

### 2. 角色与内容分离
角色信息与具体场景内容完全分离。角色定义包含完整的性格、动机和特质档案，场景中仅通过 `id` 引用。这样做的好处是：
- 角色信息可独立维护和复用
- 场景内容不冗余，保持简洁
- 便于追踪每个角色在哪些场景中出现

### 3. 内容块类型系统
每个场景的内容被拆解为四种原子类型（`action` / `dialogue` / `transition` / `note`），这是本 Schema 最关键的设计决策：

| 类型 | 用途 | 示例 |
|------|------|------|
| `action` | 动作描写、场景描述、镜头指示 | "张三推开门，雨水顺着他的脸颊滴落" |
| `dialogue` | 角色对话 | 张三对李四说的一段话 |
| `transition` | 场景切换指示 | "淡入淡出"、"切至"、"闪回" |
| `note` | 导演/编剧备注 | "此处需要特效：火焰背景" |

这种类型系统让剧本从 "散文文本" 变为 "结构化指令"，方便后续的：
- 分镜头脚本制作（只提取 action 和 transition）
- 台词本制作（只提取 dialogue）
- 拍摄日程安排（按角色筛选其所有 dialogue）

### 4. 可扩展性优先
YAML 的天然优势在于向后兼容——新增字段不会破坏现有解析器。每个节点都预留了 `tags`、`metadata` 等扩展字段，供特定项目需求使用。

---

## 完整 Schema 定义

```yaml
# ============================================================
# 剧本 YAML Schema v1.0
# ============================================================

# ---------- 顶层 ----------
script:
  meta:         ScriptMeta     # 剧本元信息
  characters:   Character[]    # 角色列表
  structure:    Structure      # 剧本结构

# ---------- ScriptMeta（剧本元信息）----------
meta:
  title:              string (必填)    # 剧本标题
  original_title:     string (选填)    # 原著小说标题
  original_author:    string (选填)    # 原著作者
  script_version:     string (必填)    # 剧本版本号，如 "1.0.0"
  genre:              string (选填)    # 题材：武侠/都市/科幻/悬疑/言情 等
  sub_genre:          string (选填)    # 次级题材
  tags:               string[] (选填)  # 标签：如 ["穿越", "系统流", "复仇"]
  language:           string (选填)    # 语言：zh-CN, en-US 等，默认 zh-CN
  source_chapters:    int (选填)       # 来源小说章节数
  word_count:         int (选填)       # 来源小说总字数
  created_at:         datetime (选填)  # 创建时间 (ISO 8601)
  notes:              string (选填)    # 整体备注

# ---------- Character（角色）----------
characters:
  - id:             string (必填)      # 唯一标识，如 "char_001"，用于场景引用
    name:           string (必填)      # 角色姓名
    alias:          string[] (选填)    # 别名/称号
    role:           string (必填)      # 角色定位：protagonist | antagonist | supporting | minor | cameo
    archetype:      string (选填)      # 角色原型：导师/盟友/信使/骗子 等
    gender:         string (选填)      # 性别
    age:            string (选填)      # 年龄（可为范围，如 "25-30"）
    appearance:     string (选填)      # 外貌描述
    personality:    string[] (选填)    # 性格特质：["冷静", "腹黑", "重情义"]
    background:     string (选填)      # 背景故事摘要
    motivation:     string (选填)      # 核心动机/欲望
    arc:            string (选填)      # 角色弧光/成长轨迹
    relationships:  (选填)             # 与其他角色的关系
      - target:     string             # 目标角色 id
        type:       string             # 关系类型：师徒/恋人/仇敌/父子 等
        description: string            # 关系描述
    notes:          string (选填)      # 角色备注

# ---------- Structure（结构）----------
structure:
  acts:             Act[] (必填)       # 幕列表

# ---------- Act（幕）----------
acts:
  - act_number:    int (必填)          # 幕序号，从 1 开始
    title:         string (选填)       # 幕标题，如 "第一幕：命运的相遇"
    synopsis:      string (选填)       # 幕的内容概要
    scenes:        Scene[] (必填)      # 场列表

# ---------- Scene（场）----------
scenes:
  - scene_number:  int (必填)          # 场序号，从 1 开始
    scene_title:   string (选填)       # 场标题
    location:      string (选填)       # 地点，如 "长安城外破庙"
    time:          string (选填)       # 时间，如 "深夜 / 暴雨"
    time_period:   string (选填)       # 时代背景提示，如 "唐朝贞观年间"
    mood:          string (选填)       # 氛围，如 "紧张 / 悲伤"
    lighting:      string (选填)       # 灯光提示，如 "昏暗，仅一盏油灯"
    characters_present: string[] (选填) # 本场出现的角色 id 列表
    source_chapter: int (选填)         # 对应小说第几章
    synopsis:      string (选填)       # 本场概要
    content:       ContentBlock[] (必填) # 内容块列表

# ---------- ContentBlock（内容块）----------
content:
  - type:          enum (必填)         # action | dialogue | transition | note
    character:     string (选填)       # 说话人角色 id（仅 dialogue 类型必填）
    character_name: string (选填)      # 说话人显示名（仅 dialogue，方便直接阅读）
    text:          string (必填)       # 内容文本
    emotion:       string (选填)       # 情感/语气（仅 dialogue），如 "愤怒 / 低声"
    subtext:       string (选填)       # 潜台词/内心活动（仅 dialogue）
    camera:        string (选填)       # 镜头指示（仅 action），如 "特写 / 中景 / 推镜"
    duration_hint: string (选填)       # 时长提示，如 "3秒" / "5分钟"
    metadata:      map (选填)          # 扩展字段，放置自定义数据

# ---------- 全局扩展 ----------
revision_history:                    # 修订历史（选填）
  - version:     string              # 版本号
    date:        datetime            # 修订日期
    author:      string              # 修订人
    changes:     string              # 修订说明
```

---

## 完整示例

```yaml
script:
  meta:
    title: "剑雨江湖"
    original_title: "剑雨江湖"
    original_author: "江湖客"
    script_version: "1.0.0"
    genre: "武侠"
    tags: ["复仇", "成长", "江湖"]
    language: "zh-CN"
    source_chapters: 5
    word_count: 15000
    created_at: "2026-06-05T10:00:00+08:00"

  characters:
    - id: "char_001"
      name: "林风"
      alias: ["风少侠", "剑痴"]
      role: "protagonist"
      archetype: "英雄"
      gender: "男"
      age: "22"
      appearance: "剑眉星目，身形修长，常着青衫"
      personality: ["坚毅", "内敛", "重情重义"]
      background: "自幼父母双亡，被师父收养于青云山，习剑十五载"
      motivation: "为父母报仇，同时守护所爱之人"
      arc: "从只知复仇的剑痴，成长为心怀天下的侠客"
      relationships:
        - target: "char_002"
          type: "恋人"
          description: "初识于江湖，历经生死后相知相守"
        - target: "char_003"
          type: "仇敌"
          description: "杀父仇人，但背后另有隐情"

    - id: "char_002"
      name: "苏婉儿"
      role: "supporting"
      gender: "女"
      age: "19"
      personality: ["聪慧", "善良", "坚韧"]
      background: "江南首富之女，因家族变故流落江湖"
      relationships:
        - target: "char_001"
          type: "恋人"
          description: "被林风所救，一路相伴"

    - id: "char_003"
      name: "铁面阎罗"
      alias: ["阎罗王"]
      role: "antagonist"
      archetype: "亦正亦邪的反派"
      gender: "男"
      age: "45"
      personality: ["冷酷", "深沉", "有自己的准则"]
      background: "曾是朝廷第一高手，因冤案叛出朝廷"

  structure:
    acts:
      - act_number: 1
        title: "第一幕：初入江湖"
        synopsis: "林风下山，初遇苏婉儿，卷入一场江湖追杀"
        scenes:
          - scene_number: 1
            scene_title: "下山"
            location: "青云山·山门"
            time: "清晨 / 薄雾"
            mood: "庄严 略带离愁"
            characters_present: ["char_001"]
            synopsis: "林风拜别师父，踏上复仇之路"
            source_chapter: 1
            content:
              - type: "action"
                text: "青云山巅，云雾缭绕。一座古朴的山门前，林风跪在青石板上。"
                camera: "远景→中景"
              - type: "dialogue"
                character: "char_001"
                character_name: "林风"
                text: "师父，弟子此去，不知何时能归。请受弟子三拜。"
                emotion: "沉静中带着不舍"
              - type: "action"
                text: "林风缓缓叩首。山风吹起他的衣袂。"
                camera: "中景"
              - type: "transition"
                text: "画面渐暗，转场：林风行于山道上，背影渐远"
              - type: "note"
                text: "此处可考虑用主题曲铺垫情绪"

          - scene_number: 2
            scene_title: "林中遇袭"
            location: "青云山下·密林"
            time: "正午"
            mood: "紧张"
            characters_present: ["char_001", "char_002"]
            synopsis: "林风遭遇追杀苏婉儿的黑衣人，出手相救"
            source_chapter: 1
            content:
              - type: "action"
                text: "密林深处，刀剑相击之声破空而来。"
                camera: "空镜→推进"
              - type: "action"
                text: "林风循声而至，只见数名黑衣人围攻一名少女。"
                camera: "过肩镜头"
              - type: "dialogue"
                character: "char_002"
                character_name: "苏婉儿"
                text: "你们到底是什么人？为何要追杀我？"
                emotion: "恐惧但倔强"
              - type: "action"
                text: "林风拔剑，剑光如电。"
                camera: "特写：剑刃出鞘"
              - type: "dialogue"
                character: "char_001"
                character_name: "林风"
                text: "以多欺少，算什么好汉。"
                emotion: "冷峻"
                subtext: "他在这些黑衣人身上看到了当年灭门惨案的影子"

  revision_history:
    - version: "1.0.0"
      date: "2026-06-05T10:00:00+08:00"
      author: "AI Script Converter"
      changes: "基于小说第1-5章自动转换生成初稿"
```

---

## 设计决策 Q&A

### Q: 为什么选择 YAML 而不是 JSON？
**A:** YAML 对人类阅读和编辑更加友好——不需要引号包裹字符串，支持多行文本（`|` 和 `>`），天然支持注释。剧本是创作者需要反复修改的文档，可编辑性优先于传输效率。同时 YAML 是 JSON 的超集，任何 YAML 都可以无损转换为 JSON 用于程序处理。

### Q: 为什么用"内容块类型系统"而不是直接写一段描述？
**A:** 剧本不同于小说，需要向演员、摄影师、灯光师等不同角色传达不同信息。将内容拆分为 `action/dialogue/transition/note` 四种类型，意味着：
- **演员**只需看 `dialogue` 块
- **摄影师**只需看 `action` 块中的 `camera` 字段
- **导演**看全部，但可以通过 `note` 捕获创作意图
- **后期剪辑**重点关注 `transition` 块

这种分离是对剧本"多读者"特性的回应。

### Q: 为什么角色信息放在顶层而不是在场景中定义？
**A:** 角色的核心信息（性格、动机、背景）是跨场景稳定的。如果在每个场景中重复描述角色，不仅冗余，还会导致版本不一致。顶层的角色档案 + 场景中的 ID 引用，是一种"写一次，处处使用"的模式，类似于编程中的"单一数据源"原则。

### Q: 如何处理小说中的内心独白/心理描写？
**A:** 小说中的内心独白通过 `subtext`（潜台词）字段承载，附加在 `dialogue` 块上。纯心理描写段落（无对话）则用 `type: note` 记录，供导演和演员理解角色内心状态，但不直接呈现在台词或动作中。这是小说到剧本转换中最需要 AI 判断的部分——什么保留为潜台词，什么外化为动作或对话。

### Q: 如何支持非中文剧本？
**A:** `meta.language` 字段使用 BCP-47 语言标签。ContentBlock 的 `text` 字段内容与语言无关。`emotion`、`camera` 等结构化字段使用英文枚举值，确保跨语言一致性。

---

## 版本演进规划

| 版本 | 计划新增 |
|------|----------|
| v1.1 | 支持多线叙事（parallel_scenes）、闪回/闪前标记 |
| v1.2 | 支持道具列表（props）、服装备注（costume） |
| v2.0 | 支持交互式剧本（分支叙事），面向游戏/互动影视 |
