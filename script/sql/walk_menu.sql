-- =============================================
-- 健步走模块菜单注册 SQL（适配 RuoYi-Vue-Plus 5.X）
-- sys_menu 20字段格式：menu_id, menu_name, parent_id, order_num, path, component,
--   query_param, is_frame, is_cache, menu_type, visible, status, perms, icon,
--   create_dept, create_by, create_time, update_by, update_time, remark
-- 超级管理员(role_id=1)无需 sys_role_menu 关联，代码层 isSuperAdmin 自动放行
-- =============================================

START TRANSACTION;

-- ----------------------------
-- 一级目录
-- ----------------------------
insert into sys_menu values(2000, '健步走管理', 0, 6, 'walk', null, '', 1, 0, 'M', '0', '0', '', 'guide', 103, 1, sysdate(), null, null, '健步走管理目录');

-- ----------------------------
-- 二级菜单（C类型）
-- ----------------------------
insert into sys_menu values(2001, '活动管理', 2000, 1, 'activity', 'walk/activity/index', '', 1, 0, 'C', '0', '0', 'walk:activity:list', 'date',      103, 1, sysdate(), null, null, '活动管理菜单');
insert into sys_menu values(2002, '会员管理', 2000, 2, 'member',   'walk/member/index',   '', 1, 0, 'C', '0', '0', 'walk:member:list',   'peoples',   103, 1, sysdate(), null, null, '会员管理菜单');
insert into sys_menu values(2003, '奖项管理', 2000, 3, 'award',    'walk/award/index',    '', 1, 0, 'C', '0', '0', 'walk:award:list',    'star',      103, 1, sysdate(), null, null, '奖项管理菜单');
insert into sys_menu values(2004, '中奖名单', 2000, 4, 'winner',   'walk/winner/index',   '', 1, 0, 'C', '0', '0', 'walk:winner:list',   'gift',      103, 1, sysdate(), null, null, '中奖名单菜单');
insert into sys_menu values(2005, '组织机构', 2000, 5, 'dept',     'walk/dept/index',     '', 1, 0, 'C', '0', '0', 'walk:dept:list',     'tree',      103, 1, sysdate(), null, null, '组织机构管理菜单');
insert into sys_menu values(2006, '排行榜',   2000, 6, 'ranking',  'walk/ranking/index',  '', 1, 0, 'C', '0', '0', 'walk:ranking:list',  'chart',     103, 1, sysdate(), null, null, '排行榜查看菜单');
insert into sys_menu values(2007, '数据统计', 2000, 7, 'stats',    'walk/stats/index',    '', 1, 0, 'C', '0', '0', 'walk:stats:list',    'chart',     103, 1, sysdate(), null, null, '数据统计菜单');
insert into sys_menu values(2008, '数据导出', 2000, 8, 'export',   'walk/export/index',   '', 1, 0, 'C', '0', '0', 'walk:export:list',   'download',  103, 1, sysdate(), null, null, '数据导出菜单');

-- ----------------------------
-- 三级按钮（F类型）
-- ----------------------------

-- 活动管理按钮
insert into sys_menu values(2101, '活动新增', 2001, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:activity:add',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2102, '活动修改', 2001, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:activity:edit',   '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2103, '活动删除', 2001, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:activity:remove', '#', 103, 1, sysdate(), null, null, '');

-- 会员管理按钮
insert into sys_menu values(2201, '会员修改', 2002, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:member:edit',     '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2202, '异常标记', 2002, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:member:abnormal', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2203, '会员删除', 2002, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:member:remove',   '#', 103, 1, sysdate(), null, null, '');

-- 奖项管理按钮
insert into sys_menu values(2301, '奖项新增', 2003, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:award:add',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2302, '奖项修改', 2003, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:award:edit',   '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2303, '奖项删除', 2003, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:award:remove', '#', 103, 1, sysdate(), null, null, '');

-- 中奖名单按钮
insert into sys_menu values(2401, '中奖新增', 2004, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:winner:add',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2402, '中奖修改', 2004, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:winner:edit',   '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2403, '中奖删除', 2004, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:winner:remove', '#', 103, 1, sysdate(), null, null, '');

-- 组织机构按钮
insert into sys_menu values(2501, '机构新增', 2005, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:dept:add',    '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2502, '机构修改', 2005, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:dept:edit',   '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2503, '机构删除', 2005, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:dept:remove', '#', 103, 1, sysdate(), null, null, '');

-- 排行榜按钮
insert into sys_menu values(2601, '刷新排行', 2006, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:ranking:refresh', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2602, '锁榜',     2006, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:ranking:lock',     '#', 103, 1, sysdate(), null, null, '');

-- 数据导出按钮
insert into sys_menu values(2701, '导出报名', 2008, 1, '', '', '', 1, 0, 'F', '0', '0', 'walk:export:members',  '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2702, '导出排行', 2008, 2, '', '', '', 1, 0, 'F', '0', '0', 'walk:export:rankings', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values(2703, '导出中奖', 2008, 3, '', '', '', 1, 0, 'F', '0', '0', 'walk:export:winners',  '#', 103, 1, sysdate(), null, null, '');

COMMIT;
