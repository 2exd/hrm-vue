package org.dromara.hrm.oss.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 通用业务-OSS文件关联对象 hrm_oss_relation
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hrm_oss_relation")
public class HrmOssRelation extends TenantEntity {

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
     * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
     */
    private String businessType;

    /**
     * 业务表主键ID（如员工表id、合同表id）
     */
    private Long businessId;

    /**
     * OSS文件主键ID
     */
    private Long ossId;


}
