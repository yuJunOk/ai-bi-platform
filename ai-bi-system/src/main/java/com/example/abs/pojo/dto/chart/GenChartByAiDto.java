package com.example.abs.pojo.dto.chart;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传请求
 * @author pengYuJun
 */
@Data
public class GenChartByAiDto implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    @Serial
    private static final long serialVersionUID = 1L;
}