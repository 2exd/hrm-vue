package org.dromara.hrm.money.controller;

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
import org.dromara.hrm.money.domain.vo.HrmEmployeeSalaryVo;
import org.dromara.hrm.money.domain.bo.HrmEmployeeSalaryBo;
import org.dromara.hrm.money.service.IHrmEmployeeSalaryService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工薪资
 *
 * @author 2exd
 * @date 2026-03-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/money/employeeSalary")
public class HrmEmployeeSalaryController extends BaseController {

    private final IHrmEmployeeSalaryService hrmEmployeeSalaryService;

    /**
     * 查询员工薪资列表
     */
    @SaCheckPermission("money:employeeSalary:list")
    @GetMapping("/list")
    public TableDataInfo<HrmEmployeeSalaryVo> list(HrmEmployeeSalaryBo bo, PageQuery pageQuery) {
        return hrmEmployeeSalaryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工薪资列表
     */
    @SaCheckPermission("money:employeeSalary:export")
    @Log(title = "员工薪资", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrmEmployeeSalaryBo bo, HttpServletResponse response) {
        List<HrmEmployeeSalaryVo> list = hrmEmployeeSalaryService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工薪资", HrmEmployeeSalaryVo.class, response);
    }

    /**
     * 获取员工薪资详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("money:employeeSalary:query")
    @GetMapping("/{id}")
    public R<HrmEmployeeSalaryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrmEmployeeSalaryService.queryById(id));
    }

    /**
     * 新增员工薪资
     */
    @SaCheckPermission("money:employeeSalary:add")
    @Log(title = "员工薪资", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrmEmployeeSalaryBo bo) {
        return toAjax(hrmEmployeeSalaryService.insertByBo(bo));
    }

    /**
     * 修改员工薪资
     */
    @SaCheckPermission("money:employeeSalary:edit")
    @Log(title = "员工薪资", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrmEmployeeSalaryBo bo) {
        return toAjax(hrmEmployeeSalaryService.updateByBo(bo));
    }

    /**
     * 删除员工薪资
     *
     * @param ids 主键串
     */
    @SaCheckPermission("money:employeeSalary:remove")
    @Log(title = "员工薪资", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrmEmployeeSalaryService.deleteWithValidByIds(List.of(ids), true));
    }
}
