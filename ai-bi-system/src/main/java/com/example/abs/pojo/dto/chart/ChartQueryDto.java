package com.example.abs.pojo.dto.chart;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
public class ChartQueryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8621951627958718531L;

    private Long id;

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

    /**
     * 用户 id
     */
    private Long userId;

}
