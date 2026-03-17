package org.dromara.hrm.oss.service;

import org.dromara.hrm.oss.domain.vo.HrmOssRelationVo;
import org.dromara.hrm.oss.domain.bo.HrmOssRelationBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 通用业务-OSS文件关联Service接口
 *
 * @author 2exd
 * @date 2026-03-15
 */
public interface IHrmOssRelationService {
    /**
     * 根据业务ID列表批量查询关联关系
     * @param businessIds 业务ID列表
     * @param businessType 业务类型
     * @return 关联列表
     */
    List<HrmOssRelationVo> queryListByBusinessIds(Collection<Long> businessIds, String businessType);

    /**
     * 查询通用业务-OSS文件关联
     *
     * @param id 主键
     * @return 通用业务-OSS文件关联
     */
    HrmOssRelationVo queryById(Long id);

    /**
     * 分页查询通用业务-OSS文件关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 通用业务-OSS文件关联分页列表
     */
    TableDataInfo<HrmOssRelationVo> queryPageList(HrmOssRelationBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的通用业务-OSS文件关联列表
     *
     * @param bo 查询条件
     * @return 通用业务-OSS文件关联列表
     */
    List<HrmOssRelationVo> queryList(HrmOssRelationBo bo);

    /**
     * 新增通用业务-OSS文件关联
     *
     * @param bo 通用业务-OSS文件关联
     * @return 是否新增成功
     */
    Boolean insertByBo(HrmOssRelationBo bo);

    /**
     * 修改通用业务-OSS文件关联
     *
     * @param bo 通用业务-OSS文件关联
     * @return 是否修改成功
     */
    Boolean updateByBo(HrmOssRelationBo bo);

    /**
     * 校验并批量删除通用业务-OSS文件关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
