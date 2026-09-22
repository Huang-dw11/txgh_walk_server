-- =====================================================================
-- 工会健步走小程序 数据库建表脚本（RuoYi-Vue-Plus 5.X 版）
-- 框架：RuoYi-Vue-Plus 5.6.2
-- 数据库：MySQL 8.0  字符集：utf8mb4
-- 业务表前缀：walk_
-- 适配要点（相对原 RuoYi 4.8.3 版本）：
--   1. 主键去除 AUTO_INCREMENT，改由 MyBatis-Plus 雪花ID(ASSIGN_ID) 生成
--   2. 审计字段 create_by/update_by 类型由 varchar(64) 改为 bigint（存用户ID），新增 create_dept bigint
--   3. 补充 walk_step_daily.is_settled 字段（原代码依赖但 SQL 缺失）
--   4. 修正示例活动 lock_time（原值未含缓冲期）
--   5. 业务表不带 tenant_id（实体忽略租户过滤）
-- 设计原则：
--   1. 明显必填字段（活动时间/名称/手机号/姓名/关联ID/日期/类型等）加 NOT NULL
--   2. 有默认值的字段填 DEFAULT，不加 NOT NULL
--   3. 可选描述字段允许为空
--   4. 保留主键(PRIMARY KEY)和普通索引(KEY)
--   5. 不建外键(FOREIGN KEY)；仅对手机号、openid等业务硬性唯一规则建UNIQUE KEY防并发重复
-- 依赖：本脚本需在 RuoYi-Vue-Plus 基础 SQL ry_vue_5.X.sql 导入后执行
-- =====================================================================

START TRANSACTION;

-- ----------------------------
-- 1. 活动表 walk_activity
-- ----------------------------
drop table if exists `walk_activity`;
create table `walk_activity` (
  `activity_id`               bigint(20)    not null                 comment '活动ID',
  `activity_name`             varchar(100)  not null                 comment '活动名称',
  `activity_theme`            varchar(255)  default null             comment '活动主题',
  `activity_start_time`       datetime      not null                 comment '活动开始时间',
  `activity_end_time`         datetime      not null                 comment '活动结束时间',
  `signup_start_time`         datetime      not null                 comment '报名开始时间',
  `signup_end_time`           datetime      not null                 comment '报名结束时间',
  `buffer_days`               int(11)       default 1                comment '缓冲期时长（天）',
  `lock_time`                 datetime      default null             comment '锁榜时间（缓冲期结束次日0点）',
  `daily_target_steps`        int(11)       default 7000             comment '每日达标步数',
  `daily_max_steps`           int(11)       default 15000            comment '单日有效步数上限',
  `reward_7days`              int(11)       default 2                comment '连续7天奖励分',
  `reward_14days`             int(11)       default 5                comment '连续14天奖励分',
  `reward_21days`             int(11)       default 10               comment '全程全勤奖励分',
  `reward_7days_max_count`    int(11)       default 3                comment '7天奖励单周期最多领取次数',
  `reward_14days_max_count`   int(11)       default 2                comment '14天奖励单周期最多领取次数',
  `activity_rule`             text          default null             comment '活动规则说明（富文本）',
  `award_setting`             text          default null             comment '奖项设置说明（富文本）',
  `member_qualification_desc` varchar(1000) default null             comment '会员获奖资格说明',
  `status`                    char(1)       default '0'              comment '活动状态（0=未开始,1=报名中,2=进行中,3=缓冲期,4=已结束）',
  `is_current`                char(1)       default 'N'              comment '是否当前活动（Y=是,N=否）',
  `create_dept`               bigint(20)    default null             comment '创建部门',
  `create_by`                 bigint(20)    default null             comment '创建者',
  `create_time`               datetime      default null             comment '创建时间',
  `update_by`                 bigint(20)    default null             comment '更新者',
  `update_time`               datetime      default null             comment '更新时间',
  `remark`                    varchar(500)  default null             comment '备注',
  `del_flag`                  char(1)       default '0'              comment '删除标志（0=存在,2=删除）',
  primary key (`activity_id`),
  key `idx_status` (`status`),
  key `idx_is_current` (`is_current`)
) engine=innodb default charset=utf8mb4 comment='活动表（含活动参数配置）';


-- ----------------------------
-- 2. 组织机构扩展表 walk_dept
-- ----------------------------
drop table if exists `walk_dept`;
create table `walk_dept` (
  `dept_id`        bigint(20)   not null                 comment '部门ID（关联sys_dept.dept_id）',
  `member_count`   int(11)      default 0                comment '会员总数（线下统计，管理员手动填入）',
  `org_level`      varchar(20)  default null             comment '机构级别（县级/镇级/单位级）',
  `contact_person` varchar(50)  default null             comment '联系人',
  `contact_phone`  varchar(20)  default null             comment '联系电话',
  `create_dept`    bigint(20)   default null             comment '创建部门',
  `create_by`      bigint(20)   default null             comment '创建者',
  `create_time`    datetime     default null             comment '创建时间',
  `update_by`      bigint(20)   default null             comment '更新者',
  `update_time`    datetime     default null             comment '更新时间',
  `remark`         varchar(500) default null             comment '备注',
  primary key (`dept_id`)
) engine=innodb default charset=utf8mb4 comment='组织机构扩展表（扩展sys_dept，随sys_dept删除）';


-- ----------------------------
-- 3. 报名会员表 walk_member
-- ----------------------------
drop table if exists `walk_member`;
create table `walk_member` (
  `member_id`               bigint(20)   not null                 comment '会员ID',
  `activity_id`             bigint(20)   not null                 comment '活动ID',
  `openid`                  varchar(64)  default null             comment '微信openid',
  `unionid`                 varchar(64)  default null             comment '微信unionid',
  `mobile`                  varchar(20)  not null                 comment '手机号（活动内业务层校验唯一）',
  `real_name`               varchar(50)  not null                 comment '姓名',
  `dept_id`                 bigint(20)   not null                 comment '所在单位（关联sys_dept）',
  `signup_time`             datetime     default null             comment '报名提交时间',
  `signup_status`           char(1)      default '0'              comment '报名状态（0=待提交草稿,1=审核通过,2=已取消,3=已停用）',
  `step_auth_status`        char(1)      default '0'              comment '微信步数授权状态（0=未授权,1=已授权）',
  `miniprogram_bind_status` char(1)      default '0'              comment '小程序绑定状态（0=未绑定,1=已绑定）',
  `is_union_member`         char(1)      default 'N'              comment '是否工会会员（Y/N，影响评奖资格）',
  `abnormal_flag`           char(1)      default '0'              comment '异常标记（0=正常,1=异常/作弊）',
  `abnormal_type`           char(1)      default null             comment '异常类型（1=刷步作弊,2=报名信息异常,3=其他）',
  `abnormal_desc`           varchar(500) default null             comment '异常描述',
  `handle_status`           char(1)      default '0'              comment '异常处理状态（0=待处理,1=已处理）',
  `handle_action`           char(1)      default null             comment '处理动作（1=清零积分,2=取消评奖资格,3=停用账号）',
  `handle_by`               varchar(64)  default null             comment '异常处理人',
  `handle_time`             datetime     default null             comment '异常处理时间',
  `create_dept`             bigint(20)   default null             comment '创建部门',
  `create_by`               bigint(20)   default null             comment '创建者',
  `create_time`             datetime     default null             comment '创建时间',
  `update_by`               bigint(20)   default null             comment '更新者',
  `update_time`             datetime     default null             comment '更新时间',
  `remark`                  varchar(500) default null             comment '备注',
  `del_flag`                char(1)      default '0'              comment '删除标志（0=存在,2=删除）',
  primary key (`member_id`),
  unique key `uk_activity_mobile` (`activity_id`, `mobile`),
  unique key `uk_activity_openid` (`activity_id`, `openid`),
  key `idx_dept` (`dept_id`),
  key `idx_signup_status` (`signup_status`),
  key `idx_abnormal_flag` (`abnormal_flag`),
  key `idx_handle_status` (`handle_status`)
) engine=innodb default charset=utf8mb4 comment='报名会员表';


-- ----------------------------
-- 4. 每日步数打卡表 walk_step_daily
-- ----------------------------
drop table if exists `walk_step_daily`;
create table `walk_step_daily` (
  `record_id`        bigint(20)  not null                 comment '记录ID',
  `activity_id`      bigint(20)  not null                 comment '活动ID',
  `member_id`        bigint(20)  not null                 comment '会员ID',
  `record_date`      date        not null                 comment '记录日期',
  `wechat_steps`     int(11)     default 0                comment '微信运动原始步数',
  `effective_steps`  int(11)     default 0                comment '有效步数（封顶daily_max_steps）',
  `is_target`        char(1)     default 'N'              comment '是否达标（Y=达标,N=未达标）',
  `is_checkin`       char(1)     default 'N'              comment '是否打卡（Y=已打卡,N=未打卡）',
  `checkin_time`     datetime    default null             comment '打卡时间',
  `is_locked`        char(1)     default 'N'              comment '是否锁定（Y=已锁定历史数据,N=可更新）',
  `is_settled`       char(1)     default 'N'              comment '该日积分是否已结算（Y=已结算,N=待结算）',
  `create_dept`      bigint(20)  default null             comment '创建部门',
  `create_by`        bigint(20)  default null             comment '创建者',
  `create_time`      datetime    default null             comment '创建时间',
  `update_by`        bigint(20)  default null             comment '更新者',
  `update_time`      datetime    default null             comment '更新时间',
  primary key (`record_id`),
  key `idx_member_date` (`member_id`, `record_date`),
  key `idx_activity_date` (`activity_id`, `record_date`)
) engine=innodb default charset=utf8mb4 comment='每日步数打卡表';


-- ----------------------------
-- 5. 步数同步日志表 walk_step_sync_log
-- ----------------------------
drop table if exists `walk_step_sync_log`;
create table `walk_step_sync_log` (
  `log_id`       bigint(20)  not null                 comment '日志ID',
  `member_id`    bigint(20)  not null                 comment '会员ID',
  `activity_id`  bigint(20)  not null                 comment '活动ID',
  `sync_date`    date        not null                 comment '同步的目标日期',
  `sync_steps`   int(11)     default 0                comment '同步步数值',
  `sync_type`    char(1)     default '1'              comment '同步类型（1=当日同步,2=历史补录）',
  `sync_time`    datetime    not null                 comment '同步时间',
  `create_dept`  bigint(20)  default null             comment '创建部门',
  `create_by`    bigint(20)  default null             comment '创建者',
  `create_time`  datetime    default null             comment '创建时间',
  `update_by`    bigint(20)  default null             comment '更新者',
  `update_time`  datetime    default null             comment '更新时间',
  primary key (`log_id`),
  key `idx_member_syncdate` (`member_id`, `sync_date`)
) engine=innodb default charset=utf8mb4 comment='步数同步日志表';


-- ----------------------------
-- 6. 积分变动明细表 walk_score_record
-- ----------------------------
drop table if exists `walk_score_record`;
create table `walk_score_record` (
  `record_id`            bigint(20)  not null                 comment '记录ID',
  `activity_id`          bigint(20)  not null                 comment '活动ID',
  `member_id`            bigint(20)  not null                 comment '会员ID',
  `score_type`           char(1)     not null                 comment '积分类型（1=每日基础分,2=连续7天奖励,3=连续14天奖励,4=21天全勤奖励）',
  `score_value`          int(11)     default 0                comment '积分变动值（正数）',
  `related_date`         date        not null                 comment '关联日期',
  `continuous_record_id` bigint(20)  default null             comment '关联连续打卡记录ID（奖励分用）',
  `create_dept`          bigint(20)  default null             comment '创建部门',
  `create_by`            bigint(20)  default null             comment '创建者',
  `create_time`          datetime    default null             comment '创建时间',
  `update_by`            bigint(20)  default null             comment '更新者',
  `update_time`          datetime    default null             comment '更新时间',
  primary key (`record_id`),
  key `idx_member_type_date` (`member_id`, `score_type`, `related_date`),
  key `idx_activity_member` (`activity_id`, `member_id`)
) engine=innodb default charset=utf8mb4 comment='积分变动明细表';


-- ----------------------------
-- 7. 积分汇总表 walk_score_summary
-- ----------------------------
drop table if exists `walk_score_summary`;
create table `walk_score_summary` (
  `summary_id`               bigint(20)  not null                 comment '汇总ID',
  `activity_id`              bigint(20)  not null                 comment '活动ID',
  `member_id`                bigint(20)  not null                 comment '会员ID',
  `base_score`               int(11)     default 0                comment '基础分合计',
  `reward_7days_score`       int(11)     default 0                comment '7天奖励分合计',
  `reward_14days_score`      int(11)     default 0                comment '14天奖励分合计',
  `reward_21days_score`      int(11)     default 0                comment '21天全勤奖励分合计',
  `total_score`              int(11)     default 0                comment '总积分',
  `total_checkin_days`       int(11)     default 0                comment '累计达标打卡天数',
  `current_continuous_days`  int(11)     default 0                comment '当前连续打卡天数',
  `max_continuous_days`      int(11)     default 0                comment '历史最长连续打卡天数',
  `total_effective_steps`    int(11)     default 0                comment '活动期累计有效步数',
  `create_dept`              bigint(20)  default null             comment '创建部门',
  `create_by`                bigint(20)  default null             comment '创建者',
  `create_time`              datetime    default null             comment '创建时间',
  `update_by`                bigint(20)  default null             comment '更新者',
  `update_time`              datetime    default null             comment '更新时间',
  primary key (`summary_id`),
  key `idx_activity_member` (`activity_id`, `member_id`),
  key `idx_total_score` (`total_score`)
) engine=innodb default charset=utf8mb4 comment='积分汇总表';


-- ----------------------------
-- 8. 连续打卡记录表 walk_continuous_checkin
-- ----------------------------
drop table if exists `walk_continuous_checkin`;
create table `walk_continuous_checkin` (
  `record_id`              bigint(20)  not null                 comment '记录ID',
  `activity_id`            bigint(20)  not null                 comment '活动ID',
  `member_id`              bigint(20)  not null                 comment '会员ID',
  `start_date`             date        not null                 comment '本段连续打卡起始日期',
  `continuous_days`        int(11)     default 0                comment '连续打卡天数',
  `is_broken`              char(1)     default 'N'              comment '是否已中断（Y=已中断,N=进行中）',
  `broken_date`            date        default null             comment '中断日期',
  `reward_7days_count`     int(11)     default 0                comment '本段已领7天奖励次数',
  `reward_14days_count`    int(11)     default 0                comment '本段已领14天奖励次数',
  `reward_21days_received` char(1)     default 'N'              comment '本段是否已领21天全勤奖励（Y/N）',
  `create_dept`            bigint(20)  default null             comment '创建部门',
  `create_by`              bigint(20)  default null             comment '创建者',
  `create_time`            datetime    default null             comment '创建时间',
  `update_by`              bigint(20)  default null             comment '更新者',
  `update_time`            datetime    default null             comment '更新时间',
  primary key (`record_id`),
  key `idx_member_broken` (`member_id`, `is_broken`),
  key `idx_activity_member` (`activity_id`, `member_id`)
) engine=innodb default charset=utf8mb4 comment='连续打卡记录表';


-- ----------------------------
-- 9. 奖项设置表 walk_award
-- ----------------------------
drop table if exists `walk_award`;
create table `walk_award` (
  `award_id`      bigint(20)    not null                 comment '奖项ID',
  `activity_id`   bigint(20)    not null                 comment '活动ID',
  `award_type`    char(1)       not null                 comment '奖项类型（1=个人奖,2=集体奖）',
  `award_level`   varchar(20)   not null                 comment '奖项等级编码（1=一等奖,2=二等奖,3=三等奖,4=优秀奖,5=先进组织单位）',
  `award_name`    varchar(100)  not null                 comment '奖项名称',
  `quota`         int(11)       default 0                comment '名额（0=若干名不限）',
  `prize_content` varchar(500)  default null             comment '奖品内容',
  `prize_value`   decimal(10,2) default 0.00             comment '奖品价值',
  `sort_order`    int(11)       default 0                comment '排序',
  `create_dept`   bigint(20)    default null             comment '创建部门',
  `create_by`     bigint(20)    default null             comment '创建者',
  `create_time`   datetime      default null             comment '创建时间',
  `update_by`     bigint(20)    default null             comment '更新者',
  `update_time`   datetime      default null             comment '更新时间',
  `remark`        varchar(500)  default null             comment '备注',
  `del_flag`      char(1)       default '0'              comment '删除标志（0=存在,2=删除）',
  primary key (`award_id`),
  key `idx_activity_type` (`activity_id`, `award_type`)
) engine=innodb default charset=utf8mb4 comment='奖项设置表';


-- ----------------------------
-- 10. 中奖名单表 walk_winner
-- ----------------------------
drop table if exists `walk_winner`;
create table `walk_winner` (
  `winner_id`      bigint(20)   not null                 comment '中奖记录ID',
  `activity_id`    bigint(20)   not null                 comment '活动ID',
  `award_id`       bigint(20)   not null                 comment '奖项ID',
  `member_id`      bigint(20)   default null             comment '会员ID（个人奖项）',
  `dept_id`        bigint(20)   default null             comment '单位ID（集体奖项）',
  `award_level`    varchar(20)  not null                 comment '奖项等级编码（冗余）',
  `receiver`       varchar(50)  default null             comment '收货人',
  `receiver_phone` varchar(20)  default null             comment '收货电话',
  `address`        varchar(500) default null             comment '收货地址',
  `win_status`     char(1)      default '0'              comment '中奖状态（0=待确认,1=已确认,2=已撤销）',
  `create_dept`    bigint(20)   default null             comment '创建部门',
  `create_by`      bigint(20)   default null             comment '创建者',
  `create_time`    datetime     default null             comment '创建时间',
  `update_by`      bigint(20)   default null             comment '更新者',
  `update_time`    datetime     default null             comment '更新时间',
  `remark`         varchar(500) default null             comment '备注',
  `del_flag`       char(1)      default '0'              comment '删除标志（0=存在,2=删除）',
  primary key (`winner_id`),
  key `idx_activity_member` (`activity_id`, `member_id`),
  key `idx_activity_dept` (`activity_id`, `dept_id`),
  key `idx_member` (`member_id`)
) engine=innodb default charset=utf8mb4 comment='中奖名单表';


-- ----------------------------
-- 11. 个人排行榜表 walk_ranking
-- ----------------------------
drop table if exists `walk_ranking`;
create table `walk_ranking` (
  `ranking_id`    bigint(20)  not null                 comment '排名ID',
  `activity_id`   bigint(20)  not null                 comment '活动ID',
  `ranking_type`  char(1)     not null                 comment '排名类型（1=当日步数,2=总步数,3=积分）',
  `record_date`   date        default null             comment '记录日期（当日排名用，总排名为空）',
  `member_id`     bigint(20)  not null                 comment '会员ID',
  `rank_no`       int(11)     default 0                comment '排名名次',
  `rank_value`    int(11)     default 0                comment '排名依据值（步数或积分）',
  `dept_id`       bigint(20)  default null             comment '所在单位（冗余）',
  `create_dept`   bigint(20)  default null             comment '创建部门',
  `create_by`     bigint(20)  default null             comment '创建者',
  `create_time`   datetime    default null             comment '创建时间',
  `update_by`     bigint(20)  default null             comment '更新者',
  `update_time`   datetime    default null             comment '更新时间',
  primary key (`ranking_id`),
  key `idx_activity_type_date` (`activity_id`, `ranking_type`, `record_date`),
  key `idx_activity_type_rank` (`activity_id`, `ranking_type`, `rank_no`)
) engine=innodb default charset=utf8mb4 comment='个人排行榜表';


-- ----------------------------
-- 12. 单位排行榜表 walk_org_ranking
-- ----------------------------
drop table if exists `walk_org_ranking`;
create table `walk_org_ranking` (
  `ranking_id`         bigint(20)    not null                 comment '排名ID',
  `activity_id`        bigint(20)    not null                 comment '活动ID',
  `dept_id`            bigint(20)    not null                 comment '单位ID',
  `member_count`       int(11)       default 0                comment '会员总数',
  `signup_count`       int(11)       default 0                comment '报名人数',
  `approved_count`     int(11)       default 0                comment '审核通过人数',
  `participation_rate` decimal(5,2)  default 0.00             comment '参与率(%)',
  `checkin_rate`       decimal(5,2)  default 0.00             comment '打卡率(%)',
  `avg_score`          decimal(8,2)  default 0.00             comment '平均积分',
  `avg_steps`          decimal(12,2) default 0.00             comment '平均步数',
  `award_count`        int(11)       default 0                comment '获奖人数',
  `org_score`          decimal(10,2) default 0.00             comment '量化计分',
  `rank_no`            int(11)       default 0                comment '单位排名',
  `create_dept`        bigint(20)    default null             comment '创建部门',
  `create_by`          bigint(20)    default null             comment '创建者',
  `create_time`        datetime      default null             comment '创建时间',
  `update_by`          bigint(20)    default null             comment '更新者',
  `update_time`        datetime      default null             comment '更新时间',
  primary key (`ranking_id`),
  key `idx_activity_dept` (`activity_id`, `dept_id`),
  key `idx_activity_rank` (`activity_id`, `rank_no`)
) engine=innodb default charset=utf8mb4 comment='单位排行榜表';


-- =====================================================================
-- 初始数据：字典类型 sys_dict_type（dict_id 从 100 起，避免与系统字典冲突）
-- 字段：(dict_id, tenant_id, dict_name, dict_type, create_dept, create_by, create_time, update_by, update_time, remark)
-- =====================================================================
insert into `sys_dict_type` values
(100, '000000', '活动状态',     'walk_activity_status',    103, 1, sysdate(), null, null, '健步走活动状态'),
(101, '000000', '报名状态',     'walk_signup_status',      103, 1, sysdate(), null, null, '健步走报名状态'),
(102, '000000', '步数授权状态', 'walk_step_auth_status',   103, 1, sysdate(), null, null, '微信步数授权状态'),
(103, '000000', '积分类型',     'walk_score_type',         103, 1, sysdate(), null, null, '健步走积分类型'),
(104, '000000', '奖项类型',     'walk_award_type',         103, 1, sysdate(), null, null, '健步走奖项类型'),
(105, '000000', '奖项等级',     'walk_award_level',        103, 1, sysdate(), null, null, '健步走奖项等级'),
(106, '000000', '排名类型',     'walk_ranking_type',       103, 1, sysdate(), null, null, '健步走排名类型'),
(107, '000000', '异常类型',     'walk_abnormal_type',      103, 1, sysdate(), null, null, '健步走异常类型'),
(108, '000000', '同步类型',     'walk_sync_type',          103, 1, sysdate(), null, null, '步数同步类型'),
(109, '000000', '中奖状态',     'walk_win_status',         103, 1, sysdate(), null, null, '中奖状态');


-- =====================================================================
-- 初始数据：字典数据 sys_dict_data（dict_code 从 100 起）
-- 字段：(dict_code, tenant_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, create_dept, create_by, create_time, update_by, update_time, remark)
-- =====================================================================
insert into `sys_dict_data` values
(100, '000000', 1, '未开始', '0', 'walk_activity_status', '', 'info',    'Y', 103, 1, sysdate(), null, null, null),
(101, '000000', 2, '报名中', '1', 'walk_activity_status', '', 'primary', 'N', 103, 1, sysdate(), null, null, null),
(102, '000000', 3, '进行中', '2', 'walk_activity_status', '', 'success', 'N', 103, 1, sysdate(), null, null, null),
(103, '000000', 4, '缓冲期', '3', 'walk_activity_status', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),
(104, '000000', 5, '已结束', '4', 'walk_activity_status', '', 'danger',  'N', 103, 1, sysdate(), null, null, null),

(110, '000000', 1, '待提交草稿', '0', 'walk_signup_status', '', 'info',    'N', 103, 1, sysdate(), null, null, null),
(111, '000000', 2, '审核通过',   '1', 'walk_signup_status', '', 'success', 'Y', 103, 1, sysdate(), null, null, null),
(112, '000000', 3, '已取消',     '2', 'walk_signup_status', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),
(113, '000000', 4, '已停用',     '3', 'walk_signup_status', '', 'danger',  'N', 103, 1, sysdate(), null, null, null),

(120, '000000', 1, '未授权', '0', 'walk_step_auth_status', '', 'danger',  'N', 103, 1, sysdate(), null, null, null),
(121, '000000', 2, '已授权', '1', 'walk_step_auth_status', '', 'success', 'Y', 103, 1, sysdate(), null, null, null),

(130, '000000', 1, '每日基础分',   '1', 'walk_score_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, null),
(131, '000000', 2, '连续7天奖励',  '2', 'walk_score_type', '', 'success', 'N', 103, 1, sysdate(), null, null, null),
(132, '000000', 3, '连续14天奖励', '3', 'walk_score_type', '', 'success', 'N', 103, 1, sysdate(), null, null, null),
(133, '000000', 4, '21天全勤奖励', '4', 'walk_score_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),

(140, '000000', 1, '个人奖', '1', 'walk_award_type', '', 'primary', 'Y', 103, 1, sysdate(), null, null, null),
(141, '000000', 2, '集体奖', '2', 'walk_award_type', '', 'success', 'N', 103, 1, sysdate(), null, null, null),

(150, '000000', 1, '一等奖',       '1', 'walk_award_level', '', 'danger',  'N', 103, 1, sysdate(), null, null, null),
(151, '000000', 2, '二等奖',       '2', 'walk_award_level', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),
(152, '000000', 3, '三等奖',       '3', 'walk_award_level', '', 'primary', 'N', 103, 1, sysdate(), null, null, null),
(153, '000000', 4, '优秀奖',       '4', 'walk_award_level', '', 'info',    'N', 103, 1, sysdate(), null, null, null),
(154, '000000', 5, '先进组织单位', '5', 'walk_award_level', '', 'success', 'N', 103, 1, sysdate(), null, null, null),

(160, '000000', 1, '当日步数', '1', 'walk_ranking_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, null),
(161, '000000', 2, '总步数',   '2', 'walk_ranking_type', '', 'success', 'N', 103, 1, sysdate(), null, null, null),
(162, '000000', 3, '积分',     '3', 'walk_ranking_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),

(170, '000000', 1, '刷步作弊',     '1', 'walk_abnormal_type', '', 'danger',  'N', 103, 1, sysdate(), null, null, null),
(171, '000000', 2, '报名信息异常', '2', 'walk_abnormal_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),
(172, '000000', 3, '其他',         '3', 'walk_abnormal_type', '', 'info',    'N', 103, 1, sysdate(), null, null, null),

(180, '000000', 1, '当日同步', '1', 'walk_sync_type', '', 'primary', 'N', 103, 1, sysdate(), null, null, null),
(181, '000000', 2, '历史补录', '2', 'walk_sync_type', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),

(190, '000000', 1, '待确认', '0', 'walk_win_status', '', 'warning', 'N', 103, 1, sysdate(), null, null, null),
(191, '000000', 2, '已确认', '1', 'walk_win_status', '', 'success', 'Y', 103, 1, sysdate(), null, null, null),
(192, '000000', 3, '已撤销', '2', 'walk_win_status', '', 'danger',  'N', 103, 1, sysdate(), null, null, null);


-- =====================================================================
-- 初始数据：示例活动（参考方案：7.30-8.19活动期，21天全勤；buffer_days=1，锁榜时间为缓冲期结束次日0点）
-- 实际使用时按需修改时间，或删除此条示例
-- =====================================================================
insert into `walk_activity` (
  `activity_id`, `activity_name`, `activity_theme`,
  `activity_start_time`, `activity_end_time`,
  `signup_start_time`, `signup_end_time`,
  `buffer_days`, `lock_time`,
  `daily_target_steps`, `daily_max_steps`,
  `reward_7days`, `reward_14days`, `reward_21days`,
  `reward_7days_max_count`, `reward_14days_max_count`,
  `activity_rule`, `award_setting`, `member_qualification_desc`,
  `status`, `is_current`,
  `create_dept`, `create_by`, `create_time`
) values (
  1,
  '藤县总工会2026年"活力职工·健康同行"线上健步走活动',
  '活力职工·健康同行',
  '2026-07-30 00:00:00', '2026-08-19 23:59:59',
  '2026-07-13 00:00:00', '2026-08-19 23:59:59',
  1, '2026-08-21 00:00:00',
  7000, 15000,
  2, 5, 10,
  3, 2,
  '活动规则：活动周期21天，每日登录小程序打卡同步步数，步数达7000步记1分；连续打卡满7天奖2分，满14天奖5分，全程21天无中断全勤奖10分。',
  '个人奖：一等奖5名/二等奖10名/三等奖20名/优秀奖若干；集体奖：先进组织单位5家。',
  '获奖名单将审核是否工会会员，非工会会员取消获奖资格。',
  '0', 'Y',
  103, 1, sysdate()
);


-- =====================================================================
-- 初始数据：示例奖项设置（对应方案3.8.1）
-- =====================================================================
insert into `walk_award` (`activity_id`, `award_type`, `award_level`, `award_name`, `quota`, `prize_content`, `prize_value`, `sort_order`, `create_dept`, `create_by`, `create_time`) values
(1, '1', '1', '一等奖',       5,  '荣誉证书 + 价值100元健身礼包', 100.00,  1, 103, 1, sysdate()),
(1, '1', '2', '二等奖',       10, '荣誉证书 + 价值80元健身礼包',  80.00,  2, 103, 1, sysdate()),
(1, '1', '3', '三等奖',       20, '荣誉证书 + 价值50元健身礼包',  50.00,  3, 103, 1, sysdate()),
(1, '1', '4', '优秀奖',       0,  '荣誉证书 + 纪念品一份',        0.00,    4, 103, 1, sysdate()),
(1, '2', '5', '先进组织单位', 5,  '奖牌 + 奖金1000元',            1000.00, 5, 103, 1, sysdate());

COMMIT;

-- =====================================================================
-- 脚本结束
-- 说明：
-- 1. 本脚本需在 RuoYi-Vue-Plus 基础 SQL（ry_vue_5.X.sql）导入后执行。
-- 2. walk_dept.dept_id 需与 sys_dept.dept_id 保持一致，随部门增删同步维护（见 walk_dept.sql）。
-- 3. 菜单注册见 walk_menu.sql；小程序端客户端配置见 walk_client.sql。
-- 4. 主键由 MyBatis-Plus 雪花ID生成，示例数据 activity_id=1 仅为演示，可按需删除。
-- 5. 业务表不带 tenant_id，实体通过 @InterceptorIgnore(tenantLine=true) 忽略租户过滤。
-- =====================================================================
