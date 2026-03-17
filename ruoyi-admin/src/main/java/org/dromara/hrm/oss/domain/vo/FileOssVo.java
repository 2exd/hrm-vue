package org.dromara.hrm.oss.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.system.domain.vo.SysOssVo;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileOssVo extends SysOssVo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
     */
    private String businessType;

    /**
     * 业务表主键ID（如员工表id、合同表id）
     */
    private Long businessId;
}
