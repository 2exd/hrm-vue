package org.dromara.hrm.money.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 员工薪资和保险详情VO
 *
 * @author 2exd
 * @date 2026-03-17
 */
@Data
@Schema(description = "员工薪资和保险详情")
public class EmployeeSalaryDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    @Schema(description = "员工ID")
    private Long userId;

    /**
     * 员工姓名
     */
    @Schema(description = "员工姓名")
    private String employeeName;

    /**
     * 查询开始月份（YYYYMM）
     */
    @Schema(description = "查询开始月份（YYYYMM）")
    private String beginMonth;

    /**
     * 查询结束月份（YYYYMM）
     */
    @Schema(description = "查询结束月份（YYYYMM）")
    private String endMonth;

    /**
     * 薪资列表
     */
    @Schema(description = "薪资列表")
    private List<HrmEmployeeSalaryVo> salaryList;

    /**
     * 五险一金及专项附加扣除列表
     */
    @Schema(description = "五险一金及专项附加扣除列表")
    private List<HrmEmployeeSocialInsuranceVo> insuranceList;

}
