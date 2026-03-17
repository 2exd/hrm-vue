package org.dromara.hrm.money.domain.vo;

import org.dromara.hrm.money.domain.HrmEmployeeSalary;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 员工薪资视图对象 hrm_employee_salary
 *
 * @author 2exd
 * @date 2026-03-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrmEmployeeSalary.class)
public class HrmEmployeeSalaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 员工ID（关联sys_user.user_id）
     */
    @ExcelProperty(value = "员工ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "关=联sys_user.user_id")
    private Long userId;

    /**
     * 员工姓名
     */
    @ExcelProperty(value = "员工姓名")
    private String employeeName;

    /**
     * 所属部门ID
     */
    @ExcelProperty(value = "所属部门ID")
    private Long deptId;

    /**
     * 所属部门名称
     */
    @ExcelProperty(value = "所属部门名称")
    private String deptName;

    /**
     * 基薪
     */
    @ExcelProperty(value = "基薪")
    private BigDecimal basicSalary;

    /**
     * 月绩效工资
     */
    @ExcelProperty(value = "月绩效工资")
    private BigDecimal monthlyPerformanceSalary;

    /**
     * 证书费
     */
    @ExcelProperty(value = "证书费")
    private BigDecimal certificateFee;

    /**
     * 岗位工资
     */
    @ExcelProperty(value = "岗位工资")
    private BigDecimal postSalary;

    /**
     * 加班工资
     */
    @ExcelProperty(value = "加班工资")
    private BigDecimal overtimeSalary;

    /**
     * 通讯补助
     */
    @ExcelProperty(value = "通讯补助")
    private BigDecimal communicationSubsidy;

    /**
     * 交通补助
     */
    @ExcelProperty(value = "交通补助")
    private BigDecimal trafficSubsidy;

    /**
     * 餐补/误餐
     */
    @ExcelProperty(value = "餐补/误餐")
    private BigDecimal mealSubsidy;

    /**
     * 其他加款
     */
    @ExcelProperty(value = "其他加款")
    private BigDecimal otherAdditional;

    /**
     * 薪资月份（格式：YYYYMM）
     */
    @ExcelProperty(value = "薪资月份", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "格=式：YYYYMM")
    private String salaryMonth;


}
