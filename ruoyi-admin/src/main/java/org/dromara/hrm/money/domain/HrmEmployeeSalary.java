package org.dromara.hrm.money.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 员工薪资对象 hrm_employee_salary
 *
 * @author 2exd
 * @date 2026-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrm_employee_salary")
public class HrmEmployeeSalary extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 员工ID（关联sys_user.user_id）
     */
    private Long userId;

    /**
     * 员工姓名
     */
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
    private Long basicSalary;

    /**
     * 月绩效工资
     */
    private Long monthlyPerformanceSalary;

    /**
     * 证书费
     */
    private Long certificateFee;

    /**
     * 岗位工资
     */
    private Long postSalary;

    /**
     * 加班工资
     */
    private Long overtimeSalary;

    /**
     * 通讯补助
     */
    private Long communicationSubsidy;

    /**
     * 交通补助
     */
    private Long trafficSubsidy;

    /**
     * 餐补/误餐
     */
    private Long mealSubsidy;

    /**
     * 其他加款
     */
    private Long otherAdditional;

    /**
     * 薪资月份（格式：YYYYMM）
     */
    private String salaryMonth;


}
