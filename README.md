# ai-bi-platform
通过AICG接口帮助我们自动生成 BI 可视化数据报表，脱离传统的生成步骤，只需将excel文件上传平台 并告诉它要得到什么样的报表，即可自动生成一张可视化报表。

## 一、需求分析

1. 智能分析：用户输入分析目标和原始数据（图表类型），即可自动生成图表和分析结论
2. 图表生成的异步化（线程池 / 消息队列）
3. 对接 AI

## 二、技术栈

### （一）前端

1. React
2. Umi + Ant Design Pro
3. Echart
4. umi openapi（自动生成前端调用接口方法）

### （二）后端

1. Java + SpringBoot框架 + MySQL
2. Hutool 工具类
3. Easy Excel 工具类
4. Swagger + Knife4j接口文档组合
5. 火山方舟AI SDK接入 
6. RabbitMQ

## 三、网站截图

### 智能分析折线图

![智能分析折线图](/doc/images/智能分析折线图.png)

### 智能分析柱状图

![智能分析柱状图](/doc/images/智能分析柱状图.png)

### 我的图表

![我的图表](/doc/images/我的图表.png)
