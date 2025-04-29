package com.example.abs.pojo.dto.chart;

import com.example.abs.pojo.dto.PageDto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
@Data
public class ChartQueryPageDto extends PageDto  implements Serializable {

    @Serial
    private static final long serialVersionUID = 6286896032699259512L;

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
