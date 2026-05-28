package com.ai.springai.reader;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 读取 markdown文件,调用其中的 loadMarkDown来把 MarkDown文档转换为 Document对象
 */
@Component
public class MarkDownReader {

    private final Resource resource;

    public MarkDownReader(@Value("classpath:text.md") Resource resource){
        this.resource = resource;
    }

    List<Document> loadMarkDown() {
        // 设置读取规则
        MarkdownDocumentReaderConfig readerConfig = MarkdownDocumentReaderConfig.builder()
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withHorizontalRuleCreateDocument(true)
                .withAdditionalMetadata("filename",resource.getFilename())
                .build();

        // 把读取规则以及源文件传入方法中
        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource,readerConfig);

        // 调用方法读取文件
        return reader.get();
    }

}
