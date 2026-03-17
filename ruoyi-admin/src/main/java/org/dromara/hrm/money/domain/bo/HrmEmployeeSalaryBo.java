package org.dromara.hrm.money.domain.bo;

import org.dromara.hrm.money.domain.HrmEmployeeSalary;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 员工薪资业务对象 hrm_employee_salary
 *
 * @author 2exd
 * @date 2026-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrmEmployeeSalary.class, reverseConvertGenerate = false)
public class HrmEmployeeSalaryBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 备注
     */
    private String remark;

    /**
     * 员工ID（关联sys_user.user_id）
     */
    @NotNull(message = "员工ID（关联sys_user.user_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String employeeName;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 基薪
     */
    private BigDecimal basicSalary;

    /**
     * 月绩效工资
     */
    private BigDecimal monthlyPerformanceSalary;

    /**
     * 证书费
     */
    private BigDecimal certificateFee;

    /**
     * 岗位工资
     */
    private BigDecimal postSalary;

    /**
     * 加班工资
     */
    private BigDecimal overtimeSalary;

    /**
     * 通讯补助
     */
    private BigDecimal communicationSubsidy;

    /**
     * 交通补助
     */
    private BigDecimal trafficSubsidy;

    /**
     * 餐补/误餐
     */
    private BigDecimal mealSubsidy;

    /**
     * 其他加款
     */
    private BigDecimal otherAdditional;

    /**
     * 薪资月份（格式：YYYYMM）
     */
    @NotBlank(message = "薪资月份（格式：YYYYMM）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String salaryMonth;


}
