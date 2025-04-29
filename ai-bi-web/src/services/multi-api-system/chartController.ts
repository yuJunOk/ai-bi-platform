// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 此处后端没有提供注释 POST /chart/add */
export async function addChart(body: API.ChartAddDto, options?: { [key: string]: any }) {
  return request<API.ResponseEntityLong>('/chart/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /chart/delete */
export async function deleteChart(body: API.IdDto, options?: { [key: string]: any }) {
  return request<API.ResponseEntityBoolean>('/chart/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /chart/gen */
export async function genChartByAi(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.genChartByAiParams,
  body: {},
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityBiResultVo>('/chart/gen', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
      genChartByAiDto: undefined,
      ...params['genChartByAiDto'],
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /chart/gen/async */
export async function genChartByAiAsync(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.genChartByAiAsyncParams,
  body: {},
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityBiResultVo>('/chart/gen/async', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
      genChartByAiDto: undefined,
      ...params['genChartByAiDto'],
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /chart/get */
export async function getChartById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getChartByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityChartDo>('/chart/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /chart/list */
export async function listChart(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listChartParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityListChartDo>('/chart/list', {
    method: 'GET',
    params: {
      ...params,
      chartQueryDto: undefined,
      ...params['chartQueryDto'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /chart/list/page */
export async function listChartByPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listChartByPageParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityPageChartDo>('/chart/list/page', {
    method: 'GET',
    params: {
      ...params,
      chartQueryPageDto: undefined,
      ...params['chartQueryPageDto'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /chart/my/list/page */
export async function listMyChartByPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listMyChartByPageParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseEntityPageChartDo>('/chart/my/list/page', {
    method: 'GET',
    params: {
      ...params,
      chartQueryPageDto: undefined,
      ...params['chartQueryPageDto'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /chart/update */
export async function updateChart(body: API.ChartUpdateDto, options?: { [key: string]: any }) {
  return request<API.ResponseEntityBoolean>('/chart/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
