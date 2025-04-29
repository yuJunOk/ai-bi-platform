package com.example.abs.pojo.dto.chart;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
@Data
public class ChartAddDto  implements Serializable {

    @Serial
    private static final long serialVersionUID = 7912335128887830432L;
    /**
     * 名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

}
