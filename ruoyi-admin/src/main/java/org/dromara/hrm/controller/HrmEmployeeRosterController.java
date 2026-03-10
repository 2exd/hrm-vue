package org.dromara.hrm.controller;

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
import org.dromara.hrm.domain.vo.HrmEmployeeRosterVo;
import org.dromara.hrm.domain.bo.HrmEmployeeRosterBo;
import org.dromara.hrm.service.IHrmEmployeeRosterService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工花名册
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrm/employeeRoster")
public class HrmEmployeeRosterController extends BaseController {

    private final IHrmEmployeeRosterService hrmEmployeeRosterService;

    /**
     * 查询员工花名册列表
     */
    @SaCheckPermission("hrm:employeeRoster:list")
    @GetMapping("/list")
    public TableDataInfo<HrmEmployeeRosterVo> list(HrmEmployeeRosterBo bo, PageQuery pageQuery) {
        return hrmEmployeeRosterService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工花名册列表
     */
    @SaCheckPermission("hrm:employeeRoster:export")
    @Log(title = "员工花名册", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrmEmployeeRosterBo bo, HttpServletResponse response) {
        List<HrmEmployeeRosterVo> list = hrmEmployeeRosterService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工花名册", HrmEmployeeRosterVo.class, response);
    }

    /**
     * 获取员工花名册详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrm:employeeRoster:query")
    @GetMapping("/{id}")
    public R<HrmEmployeeRosterVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrmEmployeeRosterService.queryById(id));
    }

    /**
     * 新增员工花名册
     */
    @SaCheckPermission("hrm:employeeRoster:add")
    @Log(title = "员工花名册", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrmEmployeeRosterBo bo) {
        return toAjax(hrmEmployeeRosterService.insertByBo(bo));
    }

    /**
     * 修改员工花名册
     */
    @SaCheckPermission("hrm:employeeRoster:edit")
    @Log(title = "员工花名册", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrmEmployeeRosterBo bo) {
        return toAjax(hrmEmployeeRosterService.updateByBo(bo));
    }

    /**
     * 删除员工花名册
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrm:employeeRoster:remove")
    @Log(title = "员工花名册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrmEmployeeRosterService.deleteWithValidByIds(List.of(ids), true));
    }
}
