package org.dromara.hrm.money.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 员工五险一金及专项附加扣除对象 hrm_employee_social_insurance
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrm_employee_social_insurance")
public class HrmEmployeeSocialInsurance extends TenantEntity {

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
     * 五险一金所属月份（格式：YYYYMM）
     */
    private Date insuranceMonth;

    /**
     * 养老保险-个人缴纳
     */
    private Long pensionPersonal;

    /**
     * 养老保险-单位缴纳
     */
    private Long pensionCompany;

    /**
     * 医疗保险-个人缴纳
     */
    private Long medicalPersonal;

    /**
     * 医疗保险-单位缴纳
     */
    private Long medicalCompany;

    /**
     * 失业保险-个人缴纳
     */
    private Long unemploymentPersonal;

    /**
     * 失业保险-单位缴纳
     */
    private Long unemploymentCompany;

    /**
     * 工伤保险-单位缴纳（个人不缴）
     */
    private Long injuryCompany;

    /**
     * 生育保险-单位缴纳（个人不缴）
     */
    private Long maternityCompany;

    /**
     * 住房公积金-个人缴纳
     */
    private Long housingFundPersonal;

    /**
     * 住房公积金-单位缴纳
     */
    private Long housingFundCompany;

    /**
     * 累计专项附加扣除-子女教育费
     */
    private Long childrenEducation;

    /**
     * 累计专项附加扣除-继续教育费
     */
    private Long continuingEducation;

    /**
     * 累计专项附加扣除-住房贷款
     */
    private Long housingLoan;

    /**
     * 累计专项附加扣除-住房租金
     */
    private Long housingRent;

    /**
     * 累计专项附加扣除-赡养老人
     */
    private Long supportElderly;

    /**
     * 累计专项附加扣除-婴幼儿专项
     */
    private Long infantCare;


}
