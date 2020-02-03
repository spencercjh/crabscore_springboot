create schema rxpb collate utf8mb4_0900_ai_ci;

create table rxpb_company
(
    id             int(11) unsigned auto_increment
        primary key,
    company_name   varchar(45)                                                                  not null comment '参选单位名: UNIQUE',
    competition_id int(11) unsigned default 1                                                   not null comment '参赛企业所属的比赛,同一企业在不同的赛事中保持独立',
    avatar_url     varchar(255)     default 'https://i.loli.net/2020/01/25/4a6thSfB5AZXmNk.jpg' null comment '参选单位头像URL',
    version        timestamp        default CURRENT_TIMESTAMP                                   not null,
    create_date    datetime         default CURRENT_TIMESTAMP                                   not null comment '数据创建日期',
    create_user    varchar(45)                                                                  null comment '数据创建用户',
    update_user    varchar(45)      default '0'                                                 null comment '数据更新用户',
    constraint unique_index_company_name
        unique (company_name)
)
    comment '企业信息表';

create index rxpb_company_info_competition_id_index
    on rxpb_company (competition_id);

create table rxpb_competition
(
    id               int(11) unsigned auto_increment
        primary key,
    competition_year varchar(45)                                                                not null comment '赛事的年份 yyyy',
    var_fatness_m    decimal(19, 4) default 0.0000                                              not null comment '雄蟹肥满度参数',
    var_fatness_f    decimal(19, 4) default 0.0000                                              not null comment '雌蟹肥满度参数',
    var_weight_m     decimal(19, 4) default 0.0000                                              not null comment '雄蟹体重参数',
    var_weight_f     decimal(19, 4) default 0.0000                                              not null comment '雌蟹体重参数',
    var_mfatness_sd  decimal(19, 4) default 0.0000                                              not null comment '雄蟹肥满度标准差参数',
    var_mweight_sd   decimal(19, 4) default 0.0000                                              not null comment '雄蟹体重参数',
    var_ffatness_sd  decimal(19, 4) default 0.0000                                              not null comment '雌蟹肥满度标准差参数',
    var_fweight_sd   decimal(19, 4) default 0.0000                                              not null comment '雌蟹体重标准差参数',
    result_fatness   tinyint        default 0                                                   not null comment '1:允许查看排名,0不允许查看排名',
    result_quality   tinyint        default 0                                                   not null comment '1:允许查看排名,0不允许查看排名',
    result_taste     tinyint        default 0                                                   not null comment '1:允许查看排名,0不允许查看排名',
    note             varchar(225)   default '备注'                                                not null comment '注备',
    status           tinyint        default 0                                                   not null comment '1：可用 0：禁用',
    avatar_url       varchar(255)   default 'https://i.loli.net/2020/01/25/4a6thSfB5AZXmNk.jpg' null comment '大赛默认图标URL',
    version          timestamp      default CURRENT_TIMESTAMP                                   not null,
    create_date      datetime       default CURRENT_TIMESTAMP                                   not null comment '数据创建日期',
    create_user      varchar(45)                                                                null comment '数据创建用户',
    update_user      varchar(45)                                                                null comment '数据更新用户',
    constraint unique_index_competition_year
        unique (competition_year)
)
    comment '大赛信息表';

create table rxpb_crab
(
    id             int unsigned auto_increment
        primary key,
    competition_id int(11) unsigned default 1                                                   not null comment '比赛ID',
    group_id       int unsigned                                                                 not null,
    crab_sex       tinyint          default -1                                                  not null comment '0:雌 1：雄',
    crab_label     varchar(32)      default '空标识'                                               not null comment '四位的蟹标识',
    crab_weight    decimal(19, 4)   default 0.0000                                              not null comment '体重',
    crab_length    decimal(19, 4)   default 0.0000                                              not null comment '壳长',
    crab_fatness   decimal(19, 4)   default 0.0000                                              not null comment '肥满度',
    avatar_url     varchar(255)     default 'https://i.loli.net/2020/01/25/4a6thSfB5AZXmNk.jpg' null comment '螃蟹图片URL',
    version        timestamp        default CURRENT_TIMESTAMP                                   not null,
    create_date    datetime         default CURRENT_TIMESTAMP                                   not null comment '数据创建日期',
    create_user    varchar(45)                                                                  null comment '数据创建用户',
    update_user    varchar(45)                                                                  null comment '数据更新用户'
)
    comment '蟹基本信息表,肥满度表';

create index rxpb_crab_info_competition_id_index
    on rxpb_crab (competition_id);

create index rxpb_crab_info_crab_sex_index
    on rxpb_crab (crab_sex);

create index rxpb_crab_info_group_id_index
    on rxpb_crab (group_id);

create table rxpb_group
(
    id              int unsigned auto_increment comment '组ID'
        primary key,
    company_id      int(11) unsigned                                                             not null comment '参赛单位id',
    competition_id  int(11) unsigned default 1                                                   not null comment '比赛ID',
    fatness_score_m decimal(19, 4)   default 0.0000                                              not null comment '雄蟹肥满度评分',
    quality_score_m decimal(19, 4)   default 0.0000                                              not null comment '雄蟹种质评分',
    taste_score_m   decimal(19, 4)   default 0.0000                                              not null comment '雄蟹口感评分',
    fatness_score_f decimal(19, 4)   default 0.0000                                              not null comment '雌蟹肥满度评分',
    quality_score_f decimal(19, 4)   default 0.0000                                              not null comment '雌蟹种质评分',
    taste_score_f   decimal(19, 4)   default 0.0000                                              not null comment '雌蟹口感评分',
    avatar_url      varchar(255)     default 'https://i.loli.net/2020/01/25/4a6thSfB5AZXmNk.jpg' null comment '小组图标URL',
    version         timestamp        default CURRENT_TIMESTAMP                                   not null,
    create_date     datetime         default CURRENT_TIMESTAMP                                   not null comment '数据创建日期',
    create_user     varchar(45)                                                                  null comment '数据创建用户',
    update_user     varchar(45)                                                                  null comment '数据更新用户'
)
    comment '组信息数表';

create index rxpb_group_info_company_id_index
    on rxpb_group (company_id);

create index rxpb_group_info_competition_id_index
    on rxpb_group (competition_id);

create table rxpb_participant
(
    id             int(11) unsigned auto_increment
        primary key,
    competition_id int(11) unsigned default 1                                                   not null comment '比赛ID',
    company_id     int(11) unsigned default 0                                                   null comment '对应的参选单位',
    role_id        int(11) unsigned                                                             not null comment '角色ID',
    username       varchar(45)                                                                  not null comment '用户名,登录名',
    password       varchar(60)                                                                  not null comment '密码',
    display_name   varchar(45)      default '未命名用户'                                             not null comment '显示名称，姓名或单位名',
    status         tinyint          default 0                                                   not null comment '用户状态 1：可用 0：禁用',
    email          varchar(45)                                                                  null,
    avatar_url     varchar(255)     default 'https://i.loli.net/2020/01/25/4a6thSfB5AZXmNk.jpg' null comment '小组图标URL',
    version        timestamp        default CURRENT_TIMESTAMP                                   not null,
    create_date    datetime         default CURRENT_TIMESTAMP                                   not null comment '数据创建日期',
    create_user    varchar(45)                                                                  null comment '数据创建用户',
    update_user    varchar(45)                                                                  null comment '数据更新用户',
    constraint rxpb_participant_username_uindex
        unique (username)
)
    comment '用户信息表';

create index rxpb_participant_company_id_index
    on rxpb_participant (company_id);

create index rxpb_participant_competition_id_index
    on rxpb_participant (competition_id);

create index rxpb_participant_display_name_index
    on rxpb_participant (display_name);

create index rxpb_participant_role_id_index
    on rxpb_participant (role_id);

create table rxpb_role
(
    id          int(11) unsigned auto_increment comment '"角色ID:1：管理员
 2：评委 3：工作人员 4:参选单位"
'
        primary key,
    role_name   varchar(15) default 'empty name'      not null,
    version     timestamp   default CURRENT_TIMESTAMP not null,
    create_date datetime    default CURRENT_TIMESTAMP not null comment '数据创建日期',
    create_user varchar(45)                           null comment '数据创建用户',
    update_user varchar(45)                           null comment '数据更新用户'
)
    comment '用户组信息表';

create index rxpb_role_info_role_name_index
    on rxpb_role (role_name);

create table rxpb_score_quality
(
    id             int(11) unsigned auto_increment
        primary key,
    competition_id int(11) unsigned default 1                 not null comment '比赛ID',
    crab_id        int unsigned                               not null comment '对应的螃蟹id',
    group_id       int unsigned                               not null comment '螃蟹对应的小组id',
    judges_id      int(11) unsigned default 0                 not null comment '打分的评委id',
    score_fin      decimal(19, 4)   default 0.0000            not null comment '最终给分',
    score_bts      decimal(19, 4)   default 0.0000            not null comment '体色(背)',
    score_fts      decimal(19, 4)   default 0.0000            not null comment '体色(腹)',
    score_ec       decimal(19, 4)   default 0.0000            not null comment '额齿',
    score_dscc     decimal(19, 4)   default 0.0000            not null comment '第4侧齿',
    score_bbyzt    decimal(19, 4)   default 0.0000            not null comment '背部疣状突',
    version        timestamp        default CURRENT_TIMESTAMP not null,
    create_date    datetime         default CURRENT_TIMESTAMP not null comment '数据创建日期',
    create_user    varchar(45)                                null comment '数据创建用户',
    update_user    varchar(45)                                null comment '数据更新用户'
)
    comment '种质评分表';

create index rxpb_score_quality_competition_id_index
    on rxpb_score_quality (competition_id);

create index rxpb_score_quality_crab_id_index
    on rxpb_score_quality (crab_id);

create index rxpb_score_quality_group_id_index
    on rxpb_score_quality (group_id);

create index rxpb_score_quality_judges_id_index
    on rxpb_score_quality (judges_id);

create table rxpb_score_taste
(
    id             int(11) unsigned auto_increment
        primary key,
    competition_id int(11) unsigned default 1                 not null comment '比赛ID',
    crab_id        int unsigned                               not null comment '对应的螃蟹id',
    group_id       int unsigned                               not null comment '螃蟹对应的小组id',
    judges_id      int(11) unsigned default 0                 not null,
    score_fin      decimal(19, 4)   default 0.0000            not null comment '最终给分',
    score_ygys     decimal(19, 4)   default 0.0000            not null comment '蟹盖颜色',
    score_sys      decimal(19, 4)   default 0.0000            not null comment '鳃颜色',
    score_ghys     decimal(19, 4)   default 0.0000            not null comment '膏、黄颜色',
    score_xwxw     decimal(19, 4)   default 0.0000            not null comment '腥味、香味',
    score_gh       decimal(19, 4)   default 0.0000            not null comment '膏、黄',
    score_fbjr     decimal(19, 4)   default 0.0000            not null comment '腹部肌肉',
    score_bzjr     decimal(19, 4)   default 0.0000            not null comment '第二、三步足肌肉',
    version        timestamp        default CURRENT_TIMESTAMP not null,
    create_date    datetime         default CURRENT_TIMESTAMP not null comment '数据创建日期',
    create_user    varchar(45)                                null comment '数据创建用户',
    update_user    varchar(45)                                null comment '数据更新用户'
)
    comment '口感奖评分表' charset = sjis;

create index rxpb_score_taste_competition_id_index
    on rxpb_score_taste (competition_id);

create index rxpb_score_taste_crab_id_index
    on rxpb_score_taste (crab_id);

create index rxpb_score_taste_group_id_index
    on rxpb_score_taste (group_id);

create index rxpb_score_taste_judges_id_index
    on rxpb_score_taste (judges_id);

