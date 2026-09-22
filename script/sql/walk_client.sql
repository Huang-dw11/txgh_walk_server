-- =============================================
-- 健步走模块-小程序客户端注册 SQL（适配 RuoYi-Vue-Plus 5.X）
-- sys_client 15字段：id, client_id, client_key, client_secret, grant_type,
--   device_type, active_timeout, timeout, status, del_flag,
--   create_dept, create_by, create_time, update_by, update_time
-- 对应 XcxAuthStrategy（grant_type=xcx, device_type=xcx）
-- =============================================

START TRANSACTION;

insert into sys_client values (3, 'be7052a7e4f802c20df10a8d131adb12', 'xcx', 'xcx123', 'xcx', 'xcx', 1800, 604800, 0, 0, 103, 1, sysdate(), null, null);

COMMIT;
