package org.dromara.hrm.oss.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.hrm.oss.domain.bo.HrmOssRelationBo;
import org.dromara.hrm.oss.domain.vo.HrmOssRelationVo;
import org.dromara.hrm.oss.domain.HrmOssRelation;
import org.dromara.hrm.oss.mapper.HrmOssRelationMapper;
import org.dromara.hrm.oss.service.IHrmOssRelationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 通用业务-OSS文件关联Service业务层处理
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrmOssRelationServiceImpl implements IHrmOssRelationService {

    private final HrmOssRelationMapper baseMapper;

    @Override
    public List<HrmOssRelationVo> queryListByBusinessIds(Collection<Long> businessIds, String businessType) {
        return baseMapper.queryListByBusinessIds(businessIds, businessType);
    }

    /**
     * 查询通用业务-OSS文件关联
     *
     * @param id 主键
     * @return 通用业务-OSS文件关联
     */
    @Override
    public HrmOssRelationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询通用业务-OSS文件关联列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 通用业务-OSS文件关联分页列表
     */
    @Override
    public TableDataInfo<HrmOssRelationVo> queryPageList(HrmOssRelationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrmOssRelation> lqw = buildQueryWrapper(bo);
        Page<HrmOssRelationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的通用业务-OSS文件关联列表
     *
     * @param bo 查询条件
     * @return 通用业务-OSS文件关联列表
     */
    @Override
    public List<HrmOssRelationVo> queryList(HrmOssRelationBo bo) {
        LambdaQueryWrapper<HrmOssRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrmOssRelation> buildQueryWrapper(HrmOssRelationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrmOssRelation> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrmOssRelation::getId);
        return lqw;
    }

    /**
     * 新增通用业务-OSS文件关联
     *
     * @param bo 通用业务-OSS文件关联
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrmOssRelationBo bo) {
        HrmOssRelation add = MapstructUtils.convert(bo, HrmOssRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改通用业务-OSS文件关联
     *
     * @param bo 通用业务-OSS文件关联
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrmOssRelationBo bo) {
        HrmOssRelation update = MapstructUtils.convert(bo, HrmOssRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrmOssRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除通用业务-OSS文件关联信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
