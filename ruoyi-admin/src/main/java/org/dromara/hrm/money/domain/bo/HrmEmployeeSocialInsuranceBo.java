package org.dromara.hrm.money.domain.bo;

import org.dromara.hrm.money.domain.HrmEmployeeSocialInsurance;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 员工五险一金及专项附加扣除业务对象 hrm_employee_social_insurance
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrmEmployeeSocialInsurance.class, reverseConvertGenerate = false)
public class HrmEmployeeSocialInsuranceBo extends BaseEntity {

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
     * 五险一金所属月份（格式：YYYYMM）
     */
    @NotNull(message = "五险一金所属月份（格式：YYYYMM）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date insuranceMonth;

    /**
     * 养老保险-个人缴纳
     */
    private BigDecimal pensionPersonal;

    /**
     * 养老保险-单位缴纳
     */
    private BigDecimal pensionCompany;

    /**
     * 医疗保险-个人缴纳
     */
    private BigDecimal medicalPersonal;

    /**
     * 医疗保险-单位缴纳
     */
    private BigDecimal medicalCompany;

    /**
     * 失业保险-个人缴纳
     */
    private BigDecimal unemploymentPersonal;

    /**
     * 失业保险-单位缴纳
     */
    private BigDecimal unemploymentCompany;

    /**
     * 工伤保险-单位缴纳（个人不缴）
     */
    private BigDecimal injuryCompany;

    /**
     * 生育保险-单位缴纳（个人不缴）
     */
    private BigDecimal maternityCompany;

    /**
     * 住房公积金-个人缴纳
     */
    private BigDecimal housingFundPersonal;

    /**
     * 住房公积金-单位缴纳
     */
    private BigDecimal housingFundCompany;

    /**
     * 累计专项附加扣除-子女教育费
     */
    private BigDecimal childrenEducation;

    /**
     * 累计专项附加扣除-继续教育费
     */
    private BigDecimal continuingEducation;

    /**
     * 累计专项附加扣除-住房贷款
     */
    private BigDecimal housingLoan;

    /**
     * 累计专项附加扣除-住房租金
     */
    private BigDecimal housingRent;

    /**
     * 累计专项附加扣除-赡养老人
     */
    private BigDecimal supportElderly;

    /**
     * 累计专项附加扣除-婴幼儿专项
     */
    private BigDecimal infantCare;

    /**
     * 扣款（罚款）
     */
    private BigDecimal deductionPenalty;

    /**
     * 扣款（借支）
     */
    private BigDecimal deductionLoan;


}
