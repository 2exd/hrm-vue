package org.dromara.hrm.money.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.dromara.hrm.money.domain.HrmEmployeeSocialInsurance;
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
 * 员工五险一金及专项附加扣除视图对象 hrm_employee_social_insurance
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrmEmployeeSocialInsurance.class)
public class HrmEmployeeSocialInsuranceVo implements Serializable {

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
     * 五险一金所属月份（格式：YYYYMM）
     */
    @ExcelProperty(value = "五险一金所属月份", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "格=式：YYYYMM")
    private Date insuranceMonth;

    /**
     * 养老保险-个人缴纳
     */
    @ExcelProperty(value = "养老保险-个人缴纳")
    private BigDecimal pensionPersonal;

    /**
     * 养老保险-单位缴纳
     */
    @ExcelProperty(value = "养老保险-单位缴纳")
    private BigDecimal pensionCompany;

    /**
     * 医疗保险-个人缴纳
     */
    @ExcelProperty(value = "医疗保险-个人缴纳")
    private BigDecimal medicalPersonal;

    /**
     * 医疗保险-单位缴纳
     */
    @ExcelProperty(value = "医疗保险-单位缴纳")
    private BigDecimal medicalCompany;

    /**
     * 失业保险-个人缴纳
     */
    @ExcelProperty(value = "失业保险-个人缴纳")
    private BigDecimal unemploymentPersonal;

    /**
     * 失业保险-单位缴纳
     */
    @ExcelProperty(value = "失业保险-单位缴纳")
    private BigDecimal unemploymentCompany;

    /**
     * 工伤保险-单位缴纳（个人不缴）
     */
    @ExcelProperty(value = "工伤保险-单位缴纳", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "个=人不缴")
    private BigDecimal injuryCompany;

    /**
     * 生育保险-单位缴纳（个人不缴）
     */
    @ExcelProperty(value = "生育保险-单位缴纳", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "个=人不缴")
    private BigDecimal maternityCompany;

    /**
     * 住房公积金-个人缴纳
     */
    @ExcelProperty(value = "住房公积金-个人缴纳")
    private BigDecimal housingFundPersonal;

    /**
     * 住房公积金-单位缴纳
     */
    @ExcelProperty(value = "住房公积金-单位缴纳")
    private BigDecimal housingFundCompany;

    /**
     * 累计专项附加扣除-子女教育费
     */
    @ExcelProperty(value = "累计专项附加扣除-子女教育费")
    private BigDecimal childrenEducation;

    /**
     * 累计专项附加扣除-继续教育费
     */
    @ExcelProperty(value = "累计专项附加扣除-继续教育费")
    private BigDecimal continuingEducation;

    /**
     * 累计专项附加扣除-住房贷款
     */
    @ExcelProperty(value = "累计专项附加扣除-住房贷款")
    private BigDecimal housingLoan;

    /**
     * 累计专项附加扣除-住房租金
     */
    @ExcelProperty(value = "累计专项附加扣除-住房租金")
    private BigDecimal housingRent;

    /**
     * 累计专项附加扣除-赡养老人
     */
    @ExcelProperty(value = "累计专项附加扣除-赡养老人")
    private BigDecimal supportElderly;

    /**
     * 累计专项附加扣除-婴幼儿专项
     */
    @ExcelProperty(value = "累计专项附加扣除-婴幼儿专项")
    private BigDecimal infantCare;

    /**
     * 扣款（罚款）
     */
    @ExcelProperty(value = "扣款-罚款")
    private BigDecimal deductionPenalty;

    /**
     * 扣款（借支）
     */
    @ExcelProperty(value = "扣款-借支")
    private BigDecimal deductionLoan;


}
