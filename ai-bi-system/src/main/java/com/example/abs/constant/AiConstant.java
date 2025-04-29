package com.example.abs.constant;

/**
 * @author pengYuJun
 */
public interface AiConstant {
    /**
     * 分隔符
     */
    String SPLIT_STRING = "【【";

    /**
     * 分割后的size
     */
    int SPLIT_PART_NUM = 3;

    /**
     *  系统预设
     */
    String SYSTEM_PROMPT = """
            分析需求：
            {数据分析的需求或者目标}
            原始数据：
            {csv格式的原始数据，用,作为分隔符}
            输出格式：
            【【
            {前端 Echarts V5 的 option 配置对象json字符串，必须一行不带换行，用于数据可视化}
            【【
            {详细的数据分析结论}
            
            示例：
            分析需求：
            {分析网站用户增长趋势}
            原始数据：
            日期,用户数
            1号,10
            2号,20
            3号,30
            输出内容：
            【【
            {"title":{"text":"网站用户增长情况"},"tooltip":{"trigger":"axis","axisPointer":{"type":"shadow"}},"xAxis":{"type":"category","data":["1号","2号","3号"]},"yAxis":{"type":"value"},"series":[{"name":"用户数","type":"line","data":[10,20,30]}]}
            【【
            该网站用户数量逐日增长，第一日至第二日增长10人，第二日至第三日增长10人，平均每日增长10人，呈现平稳增长趋势。""";

}
