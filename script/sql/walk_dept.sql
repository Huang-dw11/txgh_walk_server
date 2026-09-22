-- =============================================
-- Walk module dept data migration script
-- Adapted for RuoYi-Vue-Plus 5.X sys_dept schema
-- =============================================

ALTER TABLE `sys_dept` MODIFY `dept_name` varchar(100) NOT NULL COMMENT '部门名称';

START TRANSACTION;


-- 开启事务，任一 INSERT 失败则全部回滚

-- ----------------------------
-- 1. 插入 sys_dept（若依部门表）
-- ----------------------------
