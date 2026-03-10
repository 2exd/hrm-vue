package org.dromara.hrm.domain.bo;

import org.dromara.hrm.domain.HrmEmployeeRoster;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 员工花名册业务对象 hrm_employee_roster
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrmEmployeeRoster.class, reverseConvertGenerate = false)
public class HrmEmployeeRosterBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 系统用户ID（关联sys_user.user_id）
     */
    @NotNull(message = "系统用户ID（关联sys_user.user_id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String employeeName;

    /**
     * 身份证号（唯一）
     */
    @NotBlank(message = "身份证号（唯一）不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotNull(message = "劳动合同签订时间（进场时间）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date contractSignTime;

    /**
     * 劳动合同编码
     */
    @NotBlank(message = "劳动合同编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractCode;

    /**
     * 劳动合同到期时间
     */
    @NotNull(message = "劳动合同到期时间不能为空", groups = { AddGroup.class, EditGroup.class })
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
