declare namespace API {
  type BiResultVo = {
    genChart?: string;
    genResult?: string;
    chartId?: number;
  };

  type ChartAddDto = {
    name?: string;
    goal?: string;
    chartData?: string;
    chartType?: string;
  };

  type ChartDo = {
    id?: number;
    goal?: string;
    name?: string;
    chartData?: string;
    chartType?: string;
    genChart?: string;
    genResult?: string;
    status?: string;
    execMessage?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    deleted?: number;
  };

  type ChartQueryDto = true;

  type ChartQueryPageDto = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    name?: string;
    goal?: string;
    chartType?: string;
    userId?: number;
  };

  type ChartUpdateDto = {
    id?: number;
    name?: string;
    goal?: string;
    chartData?: string;
    chartType?: string;
    genChart?: string;
    genResult?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type genChartByAiAsyncParams = {
    genChartByAiDto: GenChartByAiDto;
  };

  type GenChartByAiDto = {
    name?: string;
    goal?: string;
    chartType?: string;
  };

  type genChartByAiParams = {
    genChartByAiDto: GenChartByAiDto;
  };

  type getChartByIdParams = {
    id: number;
  };

  type getMailCaptchaParams = {
    email: string;
  };

  type IdDto = {
    id?: number;
  };

  type IPageUserVo = {
    size?: number;
    current?: number;
    pages?: number;
    records?: UserVo[];
    total?: number;
  };

  type listChartByPageParams = {
    chartQueryPageDto: ChartQueryPageDto;
  };

  type listChartParams = {
    chartQueryDto: ChartQueryDto;
  };

  type listMyChartByPageParams = {
    chartQueryPageDto: ChartQueryPageDto;
  };

  type OrderItem = {
    column?: string;
    asc?: boolean;
  };

  type PageChartDo = {
    records?: ChartDo[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageChartDo;
    searchCount?: PageChartDo;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageDto = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
  };

  type ResponseEntityBiResultVo = {
    code?: number;
    data?: BiResultVo;
    message?: string;
  };

  type ResponseEntityBoolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type ResponseEntityChartDo = {
    code?: number;
    data?: ChartDo;
    message?: string;
  };

  type ResponseEntityInteger = {
    code?: number;
    data?: number;
    message?: string;
  };

  type ResponseEntityIPageUserVo = {
    code?: number;
    data?: IPageUserVo;
    message?: string;
  };

  type ResponseEntityListChartDo = {
    code?: number;
    data?: ChartDo[];
    message?: string;
  };

  type ResponseEntityLong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type ResponseEntityPageChartDo = {
    code?: number;
    data?: PageChartDo;
    message?: string;
  };

  type ResponseEntityUserVo = {
    code?: number;
    data?: UserVo;
    message?: string;
  };

  type searchUsersParams = {
    userDto: UserDto;
    pageDto: PageDto;
  };

  type UserDo = {
    id?: number;
    userName?: string;
    avatarUrl?: string;
    gender?: number;
    loginName?: string;
    loginPwd?: string;
    phone?: string;
    email?: string;
    status?: number;
    userRole?: number;
    createTime?: string;
    updateTime?: string;
    deleted?: number;
  };

  type UserDto = {
    id?: number;
    userName?: string;
    loginName?: string;
    avatarUrl?: string;
    gender?: number;
    phone?: string;
    email?: string;
    status?: number;
    userRole?: number;
    createTime?: string;
    deleted?: number;
  };

  type UserLoginDto = {
    loginName?: string;
    loginPwd?: string;
  };

  type UserRegisterDto = {
    loginName?: string;
    loginPwd?: string;
    checkPwd?: string;
    email?: string;
    captcha?: string;
  };

  type UserVo = {
    id?: number;
    userName?: string;
    loginName?: string;
    avatarUrl?: string;
    gender?: number;
    phone?: string;
    email?: string;
    status?: number;
    userRole?: number;
    createTime?: string;
    deleted?: number;
  };
}
