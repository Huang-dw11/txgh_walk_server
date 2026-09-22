-- ----------------------------
-- 工会健步走 SnailJob 定时任务
-- 执行前请确保 sj_job 表已由 ry_job.sql 创建
-- ----------------------------

INSERT INTO `sj_job` VALUES
(
    100, 'dev', 'walk-activity-status', 'ruoyi_group', '活动状态自动流转',
    null, 1, UNIX_TIMESTAMP() * 1000,
    1, 1, 4, 1, 'walkActivityStatusJob',
    1, '0 */5 * * * ?',
    1, 60, 3, 1, 1, 117, 1, '', 1, '', '', '活动状态自动流转（未开始→进行中→已结束）', '', 0, now(), now()
),
(
    101, 'dev', 'walk-score-ranking', 'ruoyi_group', '积分排名刷新',
    null, 1, UNIX_TIMESTAMP() * 1000,
    1, 1, 4, 1, 'walkScoreRankingJob',
    1, '0 */10 * * * ?',
    1, 60, 3, 1, 1, 118, 1, '', 1, '', '', '刷新当前活动总排名与单位排名快照', '', 0, now(), now()
);
