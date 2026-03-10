package org.dromara.hrm.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrm.domain.HrmEmployeeRoster;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 员工花名册视图对象 hrm_employee_roster
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrmEmployeeRoster.class)
public class HrmEmployeeRosterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 系统用户ID（关联sys_user.user_id）
     */
    @ExcelProperty(value = "系统用户ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "关=联sys_user.user_id")
    private Long userId;

    /**
     * 员工姓名
     */
    @ExcelProperty(value = "员工姓名")
    private String employeeName;

    /**
     * 身份证号（唯一）
     */
    @ExcelProperty(value = "身份证号", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "唯=一")
    private String idCard;

    /**
     * 电话号码
     */
    @ExcelProperty(value = "电话号码")
    private String phone;

    /**
     * 家庭住址
     */
    @ExcelProperty(value = "家庭住址")
    private String homeAddress;

    /**
     * 紧急联系人姓名
     */
    @ExcelProperty(value = "紧急联系人姓名")
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    @ExcelProperty(value = "紧急联系人电话")
    private String emergencyContactPhone;

    /**
     * 银行卡号
     */
    @ExcelProperty(value = "银行卡号")
    private String bankCard;

    /**
     * 银行卡行号
     */
    @ExcelProperty(value = "银行卡行号")
    private String bankBranchCode;

    /**
     * 开户行名称
     */
    @ExcelProperty(value = "开户行名称")
    private String bankName;

    /**
     * 日薪
     */
    @ExcelProperty(value = "日薪")
    private Long dailySalary;

    /**
     * 月薪
     */
    @ExcelProperty(value = "月薪")
    private Long monthlySalary;

    /**
     * 劳动合同签订时间（进场时间）
     */
    @ExcelProperty(value = "劳动合同签订时间", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "进=场时间")
    private Date contractSignTime;

    /**
     * 劳动合同编码
     */
    @ExcelProperty(value = "劳动合同编码")
    private String contractCode;

    /**
     * 劳动合同到期时间
     */
    @ExcelProperty(value = "劳动合同到期时间")
    private Date contractExpireTime;

    /**
     * 退场时间
     */
    @ExcelProperty(value = "退场时间")
    private Date exitTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
