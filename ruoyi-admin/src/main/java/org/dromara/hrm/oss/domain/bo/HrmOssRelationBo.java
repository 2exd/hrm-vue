package org.dromara.hrm.oss.domain.bo;

import org.dromara.hrm.oss.domain.HrmOssRelation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 通用业务-OSS文件关联业务对象 hrm_oss_relation
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HrmOssRelation.class, reverseConvertGenerate = false)
public class HrmOssRelationBo extends BaseEntity {

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
     * 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
     */
    @NotBlank(message = "业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessType;

    /**
     * 业务表主键ID（如员工表id、合同表id）
     */
    @NotNull(message = "业务表主键ID（如员工表id、合同表id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long businessId;

    /**
     * OSS文件主键ID
     */
    @NotNull(message = "OSS文件主键ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long ossId;


}
