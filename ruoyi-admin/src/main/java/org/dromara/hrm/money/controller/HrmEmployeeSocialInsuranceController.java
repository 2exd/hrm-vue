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
import org.dromara.hrm.money.domain.vo.HrmEmployeeSocialInsuranceVo;
import org.dromara.hrm.money.domain.bo.HrmEmployeeSocialInsuranceBo;
import org.dromara.hrm.money.service.IHrmEmployeeSocialInsuranceService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工五险一金及专项附加扣除
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/money/employeeSocialInsurance")
public class HrmEmployeeSocialInsuranceController extends BaseController {

    private final IHrmEmployeeSocialInsuranceService hrmEmployeeSocialInsuranceService;

    /**
     * 查询员工五险一金及专项附加扣除列表
     */
    @SaCheckPermission("money:employeeSocialInsurance:list")
    @GetMapping("/list")
    public TableDataInfo<HrmEmployeeSocialInsuranceVo> list(HrmEmployeeSocialInsuranceBo bo, PageQuery pageQuery) {
        return hrmEmployeeSocialInsuranceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工五险一金及专项附加扣除列表
     */
    @SaCheckPermission("money:employeeSocialInsurance:export")
    @Log(title = "员工五险一金及专项附加扣除", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrmEmployeeSocialInsuranceBo bo, HttpServletResponse response) {
        List<HrmEmployeeSocialInsuranceVo> list = hrmEmployeeSocialInsuranceService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工五险一金及专项附加扣除", HrmEmployeeSocialInsuranceVo.class, response);
    }

    /**
     * 获取员工五险一金及专项附加扣除详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("money:employeeSocialInsurance:query")
    @GetMapping("/{id}")
    public R<HrmEmployeeSocialInsuranceVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrmEmployeeSocialInsuranceService.queryById(id));
    }

    /**
     * 新增员工五险一金及专项附加扣除
     */
    @SaCheckPermission("money:employeeSocialInsurance:add")
    @Log(title = "员工五险一金及专项附加扣除", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrmEmployeeSocialInsuranceBo bo) {
        return toAjax(hrmEmployeeSocialInsuranceService.insertByBo(bo));
    }

    /**
     * 修改员工五险一金及专项附加扣除
     */
    @SaCheckPermission("money:employeeSocialInsurance:edit")
    @Log(title = "员工五险一金及专项附加扣除", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrmEmployeeSocialInsuranceBo bo) {
        return toAjax(hrmEmployeeSocialInsuranceService.updateByBo(bo));
    }

    /**
     * 删除员工五险一金及专项附加扣除
     *
     * @param ids 主键串
     */
    @SaCheckPermission("money:employeeSocialInsurance:remove")
    @Log(title = "员工五险一金及专项附加扣除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrmEmployeeSocialInsuranceService.deleteWithValidByIds(List.of(ids), true));
    }
}
