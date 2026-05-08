
# 一级标题：Markdown 语法测试文档

欢迎阅读这份 **Markdown 语法测试文档**，它涵盖了绝大多数常见的 Markdown 语法元素，用于验证解析器、分块器以及相关工具链的功能。

---

## 二级标题：文本与段落

这是一个普通段落，包含一些**加粗文本**、*斜体文本*以及***加粗斜体文本***。  
也可以使用下划线表示斜体：_斜体_。

段落内还可以包含 `行内代码`，例如 `System.out.println("Hello, Markdown!");`。

---

### 三级标题：列表

#### 无序列表
- 项目一
- 项目二
  - 嵌套项目 2.1
  - 嵌套项目 2.2
- 项目三

#### 有序列表
1. 第一步
2. 第二步
3. 第三步
   1. 子步骤 3.1
   2. 子步骤 3.2

#### 任务列表 (GitHub 风格)
- [x] 已完成任务
- [ ] 未完成任务
- [ ] 又一个任务

---

## 链接与图片

这是一个 [外部链接](https://spring.io/projects/spring-ai)，它指向 Spring AI 官网。

这是引用式链接：  
[Spring AI 文档][1]

[1]: https://docs.spring.io/spring-ai/reference/

![示例图片](https://via.placeholder.com/150 "这是一个占位图片")

---

## 引用块

> 这是一级引用块，通常用于引用他人的话或提示信息。
>
> > 这是嵌套的二级引用块。
> > 依然在嵌套块内。
>
> 回到一级引用。

---

## 代码块

```java
// Java 代码块
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Markdown!");
    }
}
```

```python
# Python 代码块
def greet(name):
    print(f"Hello, {name}!")
```

---

## 表格

| 姓名   | 年龄 | 城市     |
|--------|------|----------|
| Alice  | 30   | 北京     |
| Bob    | 25   | 上海     |
| Charlie| 35   | 深圳     |

| 左对齐 | 居中对齐 | 右对齐 |
|:-------|:--------:|-------:|
| 内容 A | 内容 B   | 内容 C |

---

## 数学公式 (LaTeX)

行内公式：$E = mc^2$  
独立公式：
$$
\int_0^\infty e^{-x^2} dx = \frac{\sqrt{\pi}}{2}
$$

---

## 其他行内元素

- 删除线：~~这段文本被删除了~~
- 上标：X<sup>2</sup> + Y<sup>2</sup>（部分解析器支持）
- 下标：H<sub>2</sub>O（部分解析器支持）
- 表情符号 (GitHub 风格)：:smile: :+1: :rocket:

---

## 定义列表 (扩展语法)

Markdown
:   一种轻量级标记语言，由 John Gruber 创建。

Spring AI
:   一个用于构建 AI 应用的企业