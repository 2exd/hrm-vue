package org.dromara.hrm.roster.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 员工花名册对象 hrm_employee_roster
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrm_employee_roster")
public class HrmEmployeeRoster extends TenantEntity {

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
     * 系统用户ID（关联sys_user.user_id）
     */
    private Long userId;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 身份证号（唯一）
     */
    private String idCard;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 家庭住址
     */
    private String homeAddress;

    /**
     * 紧急联系人姓名
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyContactPhone;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 银行卡行号
     */
    private String bankBranchCode;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 日薪
     */
    private Long dailySalary;

    /**
     * 月薪
     */
    private Long monthlySalary;

    /**
     * 劳动合同签订时间（进场时间）
     */
    private Date contractSignTime;

    /**
     * 劳动合同编码
     */
    private String contractCode;

    /**
     * 劳动合同到期时间
     */
    private Date contractExpireTime;

    /**
     * 退场时间
     */
    private Date exitTime;

    /**
     * 备注
     */
    private String remark;


}
