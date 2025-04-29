package com.example.abs.pojo.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Bi 的返回结果
 */
@Data
public class BiResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -8863863179860832716L;

    private String genChart;

    private String genResult;

    private Long chartId;
}
