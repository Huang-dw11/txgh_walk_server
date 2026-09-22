-- =============================================
-- 健步走模块增量 SQL（2026-07-20）
-- 用途：前端 admin 页面开发配套
--   1) 补 3 个 export 按钮权限（活动/会员/中奖）
--   2) 新增缺失字典 walk_org_level / walk_handle_action / walk_abnormal_flag
-- 适用库：txgh_walk
-- 注意：本脚本可重复执行（已加 if not exists / 子查询去重保护）
-- =============================================

START TRANSACTION;

-- ----------------------------
-- 1. 新增菜单按钮权限（F 类型）
-- ----------------------------
insert into sys_menu
select 2104, '活动导出', 2001, 4, '', '', '', 1, 0, 'F', '0', '0', 'walk:activity:export', '#', 103, 1, sysdate(), null, null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2104);

insert into sys_menu
select 2204, '会员导出', 2002, 4, '', '', '', 1, 0, 'F', '0', '0', 'walk:member:export', '#', 103, 1, sysdate(), null, null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2204);

insert into sys_menu
select 2404, '中奖导出', 2004, 4, '', '', '', 1, 0, 'F', '0', '0', 'walk:winner:export', '#', 103, 1, sysdate(), null, null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2404);


-- ----------------------------
-- 2. 新增字典类型（dict_id 从 110 起）
-- ----------------------------
insert into sys_dict_type
select 110, '000000', '机构级别', 'walk_org_level', 103, 1, sysdate(), null, null, '健步走组织机构级别'
from dual
where not exists (select 1 from sys_dict_type where dict_type = 'walk_org_level');

insert into sys_dict_type
select 111, '000000', '异常处理动作', 'walk_handle_action', 103, 1, sysdate(), null, null, '会员异常处理动作'
from dual
where not exists (select 1 from sys_dict_type where dict_type = 'walk_handle_action');

insert into sys_dict_type
select 112, '000000', '异常标记', 'walk_abnormal_flag', 103, 1, sysdate(), null, null, '正常/异常标记'
from dual
where not exists (select 1 from sys_dict_type where dict_type = 'walk_abnormal_flag');


-- ----------------------------
-- 3. 新增字典数据（dict_code 从 200 起）
-- ----------------------------

-- walk_org_level（机构级别）
insert into sys_dict_data
select 200, '000000', 1, '县级', '1', 'walk_org_level', '', 'primary', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 200);

insert into sys_dict_data
select 201, '000000', 2, '镇级', '2', 'walk_org_level', '', 'success', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 201);

insert into sys_dict_data
select 202, '000000', 3, '单位级', '3', 'walk_org_level', '', 'info', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 202);

-- walk_handle_action（异常处理动作）
insert into sys_dict_data
select 210, '000000', 1, '清零积分', '1', 'walk_handle_action', '', 'warning', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 210);

insert into sys_dict_data
select 211, '000000', 2, '取消评奖', '2', 'walk_handle_action', '', 'danger', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 211);

insert into sys_dict_data
select 212, '000000', 3, '停用账号', '3', 'walk_handle_action', '', 'danger', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 212);

-- walk_abnormal_flag（异常标记：0正常/1异常）
insert into sys_dict_data
select 220, '000000', 1, '正常', '0', 'walk_abnormal_flag', '', 'success', 'Y', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 220);

insert into sys_dict_data
select 221, '000000', 2, '异常', '1', 'walk_abnormal_flag', '', 'danger', 'N', 103, 1, sysdate(), null, null, null
from dual
where not exists (select 1 from sys_dict_data where dict_code = 221);

COMMIT;

-- ----------------------------
-- 验证（执行后查询确认）
-- ----------------------------
-- select menu_id, menu_name, perms from sys_menu where perms like 'walk:%export%' order by menu_id;
-- select dict_id, dict_type from sys_dict_type where dict_type like 'walk_%' order by dict_id;
-- select dict_code, dict_label, dict_value, dict_type from sys_dict_data
--   where dict_type in ('walk_org_level','walk_handle_action','walk_abnormal_flag') order by dict_code;
