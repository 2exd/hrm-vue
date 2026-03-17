package org.dromara.hrm.oss.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.hrm.oss.domain.HrmOssRelation;
import org.dromara.hrm.oss.domain.vo.HrmOssRelationVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Collection;
import java.util.List;

/**
 * 通用业务-OSS文件关联Mapper接口
 *
 * @author 2exd
 * @date 2026-03-15
 */
public interface HrmOssRelationMapper extends BaseMapperPlus<HrmOssRelation, HrmOssRelationVo> {

    /**
     * 根据业务ID列表查询OSS文件关联列表
     *
     * @param businessIds 业务ID列表
     * @param businessType 业务类型（EMPLOYEE/员工、CONTRACT/合同、ATTENDANCE/考勤）
     * @return OSS文件关联列表
     */
    List<HrmOssRelationVo> queryListByBusinessIds(@Param("businessIds") Collection<Long> businessIds, @Param("businessType") String businessType);

}
