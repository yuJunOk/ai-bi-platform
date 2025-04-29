package com.example.abs.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.abs.annoation.AuthCheck;
import com.example.abs.common.ResponseCode;
import com.example.abs.common.ResponseEntity;
import com.example.abs.constant.AiConstant;
import com.example.abs.constant.ChartConstant;
import com.example.abs.constant.CommonConstant;
import com.example.abs.constant.UserConstant;
import com.example.abs.exception.BusinessException;
import com.example.abs.exception.ThrowUtils;
import com.example.abs.manager.AiManager;
import com.example.abs.manager.RedisLimiterManager;
import com.example.abs.mymq.BiMessageProducer;
import com.example.abs.pojo.domain.ChartDo;
import com.example.abs.pojo.dto.IdDto;
import com.example.abs.pojo.dto.chart.*;
import com.example.abs.pojo.vo.BiResultVo;
import com.example.abs.pojo.vo.UserVo;
import com.example.abs.service.ChartService;
import com.example.abs.service.UserService;
import com.example.abs.utils.ExcelUtils;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pengYuJun
 */
@Slf4j
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private AiManager aiManager;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private BiMessageProducer biMessageProducer;

    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddDto
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    public ResponseEntity<Long> addChart(@RequestBody ChartAddDto chartAddDto, HttpServletRequest request) {
        if (chartAddDto == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        ChartDo chartDo = new ChartDo();
        BeanUtils.copyProperties(chartAddDto, chartDo);
        // 校验
        chartService.validChart(chartDo, true);
        UserVo loginUser = userService.getCurrentUser(request);
        chartDo.setUserId(loginUser.getId());
        boolean result = chartService.save(chartDo);
        if (!result) {
            throw new BusinessException(ResponseCode.ERROR);
        }
        long newChartId = chartDo.getId();
        return ResponseEntity.success(newChartId);
    }

    /**
     * 删除
     *
     * @param idDto
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    public ResponseEntity<Boolean> deleteChart(@RequestBody IdDto idDto, HttpServletRequest request) {
        if (idDto == null || idDto.getId() <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        UserVo user = userService.getCurrentUser(request);
        long id = idDto.getId();
        // 判断是否存在
        ChartDo oldChartDo = chartService.getById(id);
        if (oldChartDo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        // 仅本人或管理员可删除
        if (!oldChartDo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ResponseCode.FORBIDDEN);
        }
        boolean b = chartService.removeById(id);
        return ResponseEntity.success(b);
    }

    /**
     * 更新
     *
     * @param chartUpdateDto
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    public ResponseEntity<Boolean> updateChart(@RequestBody ChartUpdateDto chartUpdateDto,
                                                           HttpServletRequest request) {
        if (chartUpdateDto == null || chartUpdateDto.getId() <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        ChartDo chartDo = new ChartDo();
        BeanUtils.copyProperties(chartUpdateDto, chartDo);
        // 参数校验
        chartService.validChart(chartDo, false);
        UserVo user = userService.getCurrentUser(request);
        long id = chartUpdateDto.getId();
        // 判断是否存在
        ChartDo oldChartDo = chartService.getById(id);
        if (oldChartDo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        // 仅本人或管理员可修改
        if (!oldChartDo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ResponseCode.FORBIDDEN);
        }
        boolean result = chartService.updateById(chartDo);
        return ResponseEntity.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    public ResponseEntity<ChartDo> getChartById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        ChartDo chartDo = chartService.getById(id);
        return ResponseEntity.success(chartDo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param chartQueryDto
     * @return
     */
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public ResponseEntity<List<ChartDo>> listChart(ChartQueryDto chartQueryDto) {
        ChartDo chartDo = new ChartDo();
        if (chartQueryDto != null) {
            BeanUtils.copyProperties(chartQueryDto, chartDo);
        }
        QueryWrapper<ChartDo> queryWrapper = new QueryWrapper<>(chartDo);
        List<ChartDo> userInterfaceInfoList = chartService.list(queryWrapper);
        return ResponseEntity.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param chartQueryPageDto
     * @return
     */
    @AuthCheck(anyRole = {}, mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list/page")
    public ResponseEntity<Page<ChartDo>> listChartByPage(ChartQueryPageDto chartQueryPageDto) {
        if (chartQueryPageDto == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        ChartDo chartDo = new ChartDo();
        BeanUtils.copyProperties(chartQueryPageDto, chartDo);
        long current = chartQueryPageDto.getCurrent();
        long size = chartQueryPageDto.getPageSize();
        String sortField = chartQueryPageDto.getSortField();
        String sortOrder = chartQueryPageDto.getSortOrder();
        // 限制爬虫
        if (size > CommonConstant.LIMIT_PAGE_SIZE) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        QueryWrapper<ChartDo> queryWrapper = new QueryWrapper<>(chartDo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<ChartDo> userInterfaceInfoPage = chartService.page(new Page<>(current, size), queryWrapper);
        return ResponseEntity.success(userInterfaceInfoPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryPageDto
     * @param request
     * @return
     */
    @GetMapping("/my/list/page")
    public ResponseEntity<Page<ChartDo>> listMyChartByPage(ChartQueryPageDto chartQueryPageDto, HttpServletRequest request) {
        if (chartQueryPageDto == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        //
        ChartDo chartDo = new ChartDo();
        BeanUtils.copyProperties(chartQueryPageDto, chartDo);
        long current = chartQueryPageDto.getCurrent();
        long size = chartQueryPageDto.getPageSize();
        String sortField = chartQueryPageDto.getSortField();
        String sortOrder = chartQueryPageDto.getSortOrder();
        //当前用户信息
        UserVo loginUser = userService.getCurrentUser(request);
        chartDo.setUserId(loginUser.getId());
        // 限制爬虫
        if (size > CommonConstant.LIMIT_PAGE_SIZE) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        QueryWrapper<ChartDo> queryWrapper = new QueryWrapper<>(chartDo);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), StrUtil.toUnderlineCase(sortField));
        Page<ChartDo> userInterfaceInfoPage = chartService.page(new Page<>(current, size), queryWrapper);
        return ResponseEntity.success(userInterfaceInfoPage);
    }

    // endregion

    /**
     * 智能分析（同步）
     *
     * @param file
     * @param genChartByAiDto
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public ResponseEntity<BiResultVo> genChartByAi(@RequestPart("file") MultipartFile file,
                                                   GenChartByAiDto genChartByAiDto, HttpServletRequest request) {
        String name = genChartByAiDto.getName();
        String goal = genChartByAiDto.getGoal();
        String chartType = genChartByAiDto.getChartType();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ResponseCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ResponseCode.PARAMS_ERROR, "名称过长");
        // 校验文件
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ResponseCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = List.of("xlsx");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ResponseCode.PARAMS_ERROR, "文件后缀非法");
        // 限流判断，每个用户一个限流器
        UserVo loginUser = userService.getCurrentUser(request);
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(file);
        userInput.append(csvData).append("\n");
        // 调用AI
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(AiConstant.SYSTEM_PROMPT).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userInput.toString()).build();
        String answer = aiManager.doChat(List.of(systemMessage, userMessage));
        //
        String[] splits = answer.split(AiConstant.SPLIT_STRING);
        if (splits.length != AiConstant.SPLIT_PART_NUM) {
            throw new BusinessException(ResponseCode.ERROR, "AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 插入到数据库
        ChartDo chart = new ChartDo();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setStatus(ChartConstant.SUCCESS_STATUS);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ResponseCode.ERROR, "图表保存失败");
        // 返回结果
        BiResultVo biResponse = new BiResultVo();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());
        return ResponseEntity.success(biResponse);
    }

    /**
     * 智能分析（异步）
     *
     * @param file
     * @param genChartByAiDto
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public ResponseEntity<BiResultVo> genChartByAiAsync(@RequestPart("file") MultipartFile file,
                                                   GenChartByAiDto genChartByAiDto, HttpServletRequest request) {
        String name = genChartByAiDto.getName();
        String goal = genChartByAiDto.getGoal();
        String chartType = genChartByAiDto.getChartType();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ResponseCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ResponseCode.PARAMS_ERROR, "名称过长");
        // 校验文件
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ResponseCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = List.of("xlsx");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ResponseCode.PARAMS_ERROR, "文件后缀非法");
        // 限流判断，每个用户一个限流器
        UserVo loginUser = userService.getCurrentUser(request);
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(file);
        userInput.append(csvData).append("\n");
        // 插入到数据库
        ChartDo chart = new ChartDo();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus(ChartConstant.WAIT_STATUS);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ResponseCode.ERROR, "图表保存失败");

        CompletableFuture.runAsync(() -> {
            // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
            ChartDo updateChart = new ChartDo();
            updateChart.setId(chart.getId());
            updateChart.setStatus(ChartConstant.RUNNING_STATUS);
            boolean b = chartService.updateById(updateChart);
            if (!b) {
                handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
                return;
            }
            // 调用AI
            final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(AiConstant.SYSTEM_PROMPT).build();
            final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userInput.toString()).build();
            String answer = aiManager.doChat(List.of(systemMessage, userMessage));
            //
            String[] splits = answer.split(AiConstant.SPLIT_STRING);
            if (splits.length != AiConstant.SPLIT_PART_NUM) {
                handleChartUpdateError(chart.getId(), "AI 生成错误");
                return;
            }
            String genChart = splits[1].trim();
            String genResult = splits[2].trim();
            // 更新到数据库
            updateChart = new ChartDo();
            updateChart.setId(chart.getId());
            updateChart.setGenChart(genChart);
            updateChart.setGenResult(genResult);
            updateChart.setStatus(ChartConstant.SUCCESS_STATUS);
            boolean updateResult = chartService.updateById(updateChart);
            if (!updateResult) {
                handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
            }
        }, threadPoolExecutor);

        // 返回结果
        BiResultVo biResponse = new BiResultVo();
        biResponse.setChartId(chart.getId());
        return ResponseEntity.success(biResponse);
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        ChartDo updateChartResult = new ChartDo();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(ChartConstant.FAILED_STATUS);
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

    /**
     * 智能分析（异步）
     *
     * @param file
     * @param genChartByAiDto
     * @param request
     * @return
     */
    @PostMapping("/gen/async/mq")
    public ResponseEntity<BiResultVo> genChartByAiAsyncMq(@RequestPart("file") MultipartFile file,
                                                        GenChartByAiDto genChartByAiDto, HttpServletRequest request) {
        String name = genChartByAiDto.getName();
        String goal = genChartByAiDto.getGoal();
        String chartType = genChartByAiDto.getChartType();
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ResponseCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ResponseCode.PARAMS_ERROR, "名称过长");
        // 校验文件
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ResponseCode.PARAMS_ERROR, "文件超过 1M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = List.of("xlsx");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ResponseCode.PARAMS_ERROR, "文件后缀非法");
        // 限流判断，每个用户一个限流器
        UserVo loginUser = userService.getCurrentUser(request);
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(file);
        userInput.append(csvData).append("\n");
        // 插入到数据库
        ChartDo chart = new ChartDo();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus(ChartConstant.WAIT_STATUS);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        ThrowUtils.throwIf(!saveResult, ResponseCode.ERROR, "图表保存失败");

        // 异步消息队列
        biMessageProducer.sendMessage(String.valueOf(chart.getId()));

        // 返回结果
        BiResultVo biResponse = new BiResultVo();
        biResponse.setChartId(chart.getId());
        return ResponseEntity.success(biResponse);
    }
}
