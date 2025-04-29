create database db_multi

create table tb_user
(
    id          bigint auto_increment comment 'id'
        primary key,
    user_name   varchar(256)                       null comment '用户名',
    avatar_url  varchar(1024)                      null comment '头像链接',
    gender      tinyint                            null comment '性别',
    login_name  varchar(256)                       null comment '账号',
    login_pwd   varchar(512)                       not null comment '密码',
    phone       varchar(128)                       null comment '电话',
    email       varchar(512)                       null comment '邮箱',
    status      int      default 0                 not null comment '用户状态',
    user_role   int      default 0                 not null comment '用户角色 0为普通用户 1为管理员',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint  default 0                 not null comment '是否删除',
    constraint uni_login_name
        unique (login_name)
)
    comment '用户表';

INSERT INTO `db_multi`.`tb_user` (`user_name`, `avatar_url`, `gender`, `login_name`, `login_pwd`, `phone`, `email`, `status`, `user_role`, `create_time`, `update_time`, `deleted`) VALUES ('小a', 'https://vitejs.cn/vite3-cn/logo.svg', 0, '13416393834', '3371e00731ba0313ea06d12d97f0844e', '13416393834', '1375841038@qq.com', 0, 1, '2025-04-15 00:06:41', '2025-04-15 00:06:41', 0);

create table tb_chart
(
    id           bigint auto_increment comment 'id'
        primary key,
    goal         text                                   null comment '分析目标',
    name         varchar(128)                           null comment '图表名称',
    chart_data   text                                   null comment '图表数据',
    chart_type   varchar(128)                           null comment '图表类型',
    gen_chart    text                                   null comment '生成的图表数据',
    gen_result   text                                   null comment '生成的分析结论',
    status       varchar(128) default 'wait'            not null comment 'wait,running,succeed,failed',
    exec_message text                                   null comment '执行信息',
    user_id      bigint                                 null comment '创建用户 id',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      tinyint      default 0                 not null comment '是否删除'
)
    comment '图表信息表';

INSERT INTO `db_multi`.`tb_chart` (`goal`, `name`, `chart_data`, `chart_type`, `gen_chart`, `gen_result`, `status`, `exec_message`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES ('分析网站用户的增长情况', '用户增长情况', '日期,用户数\n1号,10\n2号,20\n3号,30\n', '折线图', '{\"title\":{\"text\":\"网站用户增长情况\"},\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"xAxis\":{\"type\":\"category\",\"data\":[\"1号\",\"2号\",\"3号\"]},\"yAxis\":{\"type\":\"value\"},\"series\":[{\"name\":\"用户数\",\"type\":\"line\",\"data\":[10,20,30]}]}', '该网站用户数量逐日增长，第一日至第二日增长10人，第二日至第三日增长10人，平均每日增长10人，呈现平稳增长趋势。', 'success', NULL, 1, '2025-04-28 02:59:07', '2025-04-28 02:59:07', 0);
INSERT INTO `db_multi`.`tb_chart` (`goal`, `name`, `chart_data`, `chart_type`, `gen_chart`, `gen_result`, `status`, `exec_message`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES ('分析网站用户的增长情况', '用户增长情况', '日期,用户数\n1号,10\n2号,20\n3号,30\n', '柱状图', '{\"title\":{\"text\":\"网站用户增长情况\"},\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"data\":[\"用户数\"]},\"xAxis\":{\"type\":\"category\",\"data\":[\"1号\",\"2号\",\"3号\"]},\"yAxis\":{\"type\":\"value\"},\"series\":[{\"name\":\"用户数\",\"type\":\"bar\",\"stack\":\"total\",\"data\":[10,20,30]}]}', '该网站用户数量逐日增长，第一日至第二日增长10人，第二日至第三日增长10人，平均每日增长10人，呈现平稳增长趋势。堆叠图清晰地展示了每日的用户增长情况，便于观察和分析用户增长的趋势。', 'success', NULL, 1, '2025-04-28 20:09:14', '2025-04-28 20:12:36', 0);

