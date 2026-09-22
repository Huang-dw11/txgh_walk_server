package org.dromara.walk.controller.h5;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.service.IWalkActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/h5")
@Tag(name = "H5活动信息")
public class H5ActivityController {

    private final IWalkActivityService activityService;

    @Operation(summary = "获取当前活动信息")
    @GetMapping("/activities/current")
    public R<WalkActivityVo> currentActivity() {
        WalkActivityVo activity = activityService.selectCurrentActivity();
        if (activity == null) {
            return R.fail("暂无活动");
        }
        return R.ok(activity);
    }
}
