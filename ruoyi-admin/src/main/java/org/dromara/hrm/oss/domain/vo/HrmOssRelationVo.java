package org.dromara.hrm.oss.domain.vo;

import org.dromara.hrm.oss.domain.HrmOssRelation;
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
 * 通用业务-OSS文件关联视图对象 hrm_oss_relation
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrmOssRelation.class)
public class HrmOssRelationVo implements Serializable {

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
     * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
     */
    @ExcelProperty(value = "业务类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "E=MPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤")
    private String businessType;

    /**
     * 业务表主键ID（如员工表id、合同表id）
     */
    @ExcelProperty(value = "业务表主键ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "如=员工表id、合同表id")
    private Long businessId;

    /**
     * OSS文件主键ID
     */
    @ExcelProperty(value = "OSS文件主键ID")
    private Long ossId;


}
