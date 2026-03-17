package org.dromara.hrm.money.service.impl;

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
import org.dromara.hrm.money.domain.bo.HrmEmployeeSocialInsuranceBo;
import org.dromara.hrm.money.domain.vo.HrmEmployeeSocialInsuranceVo;
import org.dromara.hrm.money.domain.HrmEmployeeSocialInsurance;
import org.dromara.hrm.money.mapper.HrmEmployeeSocialInsuranceMapper;
import org.dromara.hrm.money.service.IHrmEmployeeSocialInsuranceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工五险一金及专项附加扣除Service业务层处理
 *
 * @author 2exd
 * @date 2026-03-15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrmEmployeeSocialInsuranceServiceImpl implements IHrmEmployeeSocialInsuranceService {

    private final HrmEmployeeSocialInsuranceMapper baseMapper;

    /**
     * 查询员工五险一金及专项附加扣除
     *
     * @param id 主键
     * @return 员工五险一金及专项附加扣除
     */
    @Override
    public HrmEmployeeSocialInsuranceVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询员工五险一金及专项附加扣除列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工五险一金及专项附加扣除分页列表
     */
    @Override
    public TableDataInfo<HrmEmployeeSocialInsuranceVo> queryPageList(HrmEmployeeSocialInsuranceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrmEmployeeSocialInsurance> lqw = buildQueryWrapper(bo);
        Page<HrmEmployeeSocialInsuranceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工五险一金及专项附加扣除列表
     *
     * @param bo 查询条件
     * @return 员工五险一金及专项附加扣除列表
     */
    @Override
    public List<HrmEmployeeSocialInsuranceVo> queryList(HrmEmployeeSocialInsuranceBo bo) {
        LambdaQueryWrapper<HrmEmployeeSocialInsurance> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrmEmployeeSocialInsurance> buildQueryWrapper(HrmEmployeeSocialInsuranceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrmEmployeeSocialInsurance> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrmEmployeeSocialInsurance::getId);
        lqw.like(StringUtils.isNotBlank(bo.getDeptName()), HrmEmployeeSocialInsurance::getDeptName, bo.getDeptName());
        lqw.between(params.get("beginInsuranceMonth") != null && params.get("endInsuranceMonth") != null,
            HrmEmployeeSocialInsurance::getInsuranceMonth ,params.get("beginInsuranceMonth"), params.get("endInsuranceMonth"));
        return lqw;
    }

    /**
     * 新增员工五险一金及专项附加扣除
     *
     * @param bo 员工五险一金及专项附加扣除
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrmEmployeeSocialInsuranceBo bo) {
        HrmEmployeeSocialInsurance add = MapstructUtils.convert(bo, HrmEmployeeSocialInsurance.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改员工五险一金及专项附加扣除
     *
     * @param bo 员工五险一金及专项附加扣除
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrmEmployeeSocialInsuranceBo bo) {
        HrmEmployeeSocialInsurance update = MapstructUtils.convert(bo, HrmEmployeeSocialInsurance.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrmEmployeeSocialInsurance entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工五险一金及专项附加扣除信息
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
