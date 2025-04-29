package com.example.abs.service;

import com.example.abs.pojo.domain.ChartDo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author pengYuJun
* @description 针对表【tb_chart(图表信息表)】的数据库操作Service
* @createDate 2025-04-25 01:12:35
*/
public interface ChartService extends IService<ChartDo> {
    /**
     * 验证信息有效性
     * @param chartDo
     * @param add
     */
    void validChart(ChartDo chartDo, boolean add);
}
