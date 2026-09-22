package org.dromara.walk.controller.h5;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.vo.SysDeptVo;
import org.dromara.system.mapper.SysDeptMapper;
import org.dromara.walk.domain.bo.WalkDeptBo;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.mapper.WalkDeptMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/h5/depts")
@Tag(name = "H5组织机构")
public class H5DeptController {

    private final SysDeptMapper sysDeptMapper;
    private final WalkDeptMapper walkDeptMapper;

    @Operation(summary = "获取组织机构列表")
    @GetMapping
    public R<List<SysDeptVo>> deptList() {
        Set<Long> walkDeptIds = walkDeptMapper.selectWalkDeptList(new WalkDeptBo())
                .stream().map(WalkDeptVo::getDeptId).collect(Collectors.toSet());

        List<SysDeptVo> deptList = sysDeptMapper.selectVoList(
            new LambdaQueryWrapper<SysDept>().eq(SysDept::getStatus, "0")
        );

        List<SysDeptVo> filtered = deptList.stream()
                .filter(d -> walkDeptIds.contains(d.getDeptId()))
                .collect(Collectors.toList());

        return R.ok(filtered);
    }
}
