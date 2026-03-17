package org.dromara.hrm.oss.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.hrm.oss.domain.vo.HrmOssRelationVo;
import org.dromara.hrm.oss.domain.bo.HrmOssRelationBo;
import org.dromara.hrm.oss.service.IHrmOssRelationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 通用业务-OSS文件关联
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrm/ossRelation")
public class HrmOssRelationController extends BaseController {

    private final IHrmOssRelationService hrmOssRelationService;

    /**
     * 查询通用业务-OSS文件关联列表
     */
    @SaCheckPermission("hrm:ossRelation:list")
    @GetMapping("/list")
    public TableDataInfo<HrmOssRelationVo> list(HrmOssRelationBo bo, PageQuery pageQuery) {
        return hrmOssRelationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出通用业务-OSS文件关联列表
     */
    @SaCheckPermission("hrm:ossRelation:export")
    @Log(title = "通用业务-OSS文件关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrmOssRelationBo bo, HttpServletResponse response) {
        List<HrmOssRelationVo> list = hrmOssRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "通用业务-OSS文件关联", HrmOssRelationVo.class, response);
    }

    /**
     * 获取通用业务-OSS文件关联详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrm:ossRelation:query")
    @GetMapping("/{id}")
    public R<HrmOssRelationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrmOssRelationService.queryById(id));
    }

    /**
     * 新增通用业务-OSS文件关联
     */
    @SaCheckPermission("hrm:ossRelation:add")
    @Log(title = "通用业务-OSS文件关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrmOssRelationBo bo) {
        return toAjax(hrmOssRelationService.insertByBo(bo));
    }

    /**
     * 修改通用业务-OSS文件关联
     */
    @SaCheckPermission("hrm:ossRelation:edit")
    @Log(title = "通用业务-OSS文件关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrmOssRelationBo bo) {
        return toAjax(hrmOssRelationService.updateByBo(bo));
    }

    /**
     * 删除通用业务-OSS文件关联
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrm:ossRelation:remove")
    @Log(title = "通用业务-OSS文件关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrmOssRelationService.deleteWithValidByIds(List.of(ids), true));
    }
}
