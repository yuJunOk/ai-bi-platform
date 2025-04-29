package com.example.abs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.abs.pojo.domain.ChartDo;
import com.example.abs.service.ChartService;
import com.example.abs.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author pengYuJun
* @description 针对表【tb_chart(图表信息表)】的数据库操作Service实现
* @createDate 2025-04-25 01:12:35
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, ChartDo>
    implements ChartService{

    @Override
    public void validChart(ChartDo chartDo, boolean add) {

    }
}




