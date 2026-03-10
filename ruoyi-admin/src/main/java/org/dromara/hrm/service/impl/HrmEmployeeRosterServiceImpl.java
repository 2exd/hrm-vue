package org.dromara.hrm.service.impl;

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
import org.dromara.hrm.domain.bo.HrmEmployeeRosterBo;
import org.dromara.hrm.domain.vo.HrmEmployeeRosterVo;
import org.dromara.hrm.domain.HrmEmployeeRoster;
import org.dromara.hrm.mapper.HrmEmployeeRosterMapper;
import org.dromara.hrm.service.IHrmEmployeeRosterService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工花名册Service业务层处理
 *
 * @author Lion Li
 * @date 2026-03-10
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrmEmployeeRosterServiceImpl implements IHrmEmployeeRosterService {

    private final HrmEmployeeRosterMapper baseMapper;

    /**
     * 查询员工花名册
     *
     * @param id 主键
     * @return 员工花名册
     */
    @Override
    public HrmEmployeeRosterVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询员工花名册列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工花名册分页列表
     */
    @Override
    public TableDataInfo<HrmEmployeeRosterVo> queryPageList(HrmEmployeeRosterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrmEmployeeRoster> lqw = buildQueryWrapper(bo);
        Page<HrmEmployeeRosterVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工花名册列表
     *
     * @param bo 查询条件
     * @return 员工花名册列表
     */
    @Override
    public List<HrmEmployeeRosterVo> queryList(HrmEmployeeRosterBo bo) {
        LambdaQueryWrapper<HrmEmployeeRoster> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrmEmployeeRoster> buildQueryWrapper(HrmEmployeeRosterBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrmEmployeeRoster> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrmEmployeeRoster::getId);
        lqw.eq(bo.getUserId() != null, HrmEmployeeRoster::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getEmployeeName()), HrmEmployeeRoster::getEmployeeName, bo.getEmployeeName());
        lqw.like(StringUtils.isNotBlank(bo.getIdCard()), HrmEmployeeRoster::getIdCard, bo.getIdCard());
        lqw.like(StringUtils.isNotBlank(bo.getPhone()), HrmEmployeeRoster::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getHomeAddress()), HrmEmployeeRoster::getHomeAddress, bo.getHomeAddress());
        lqw.like(StringUtils.isNotBlank(bo.getEmergencyContact()), HrmEmployeeRoster::getEmergencyContact, bo.getEmergencyContact());
        lqw.like(StringUtils.isNotBlank(bo.getEmergencyContactPhone()), HrmEmployeeRoster::getEmergencyContactPhone, bo.getEmergencyContactPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getBankCard()), HrmEmployeeRoster::getBankCard, bo.getBankCard());
        lqw.eq(StringUtils.isNotBlank(bo.getBankBranchCode()), HrmEmployeeRoster::getBankBranchCode, bo.getBankBranchCode());
        lqw.like(StringUtils.isNotBlank(bo.getBankName()), HrmEmployeeRoster::getBankName, bo.getBankName());
        lqw.eq(bo.getDailySalary() != null, HrmEmployeeRoster::getDailySalary, bo.getDailySalary());
        lqw.eq(bo.getMonthlySalary() != null, HrmEmployeeRoster::getMonthlySalary, bo.getMonthlySalary());
        lqw.between(params.get("beginContractSignTime") != null && params.get("endContractSignTime") != null,
            HrmEmployeeRoster::getContractSignTime ,params.get("beginContractSignTime"), params.get("endContractSignTime"));
        lqw.like(StringUtils.isNotBlank(bo.getContractCode()), HrmEmployeeRoster::getContractCode, bo.getContractCode());
        lqw.between(params.get("beginContractExpireTime") != null && params.get("endContractExpireTime") != null,
            HrmEmployeeRoster::getContractExpireTime ,params.get("beginContractExpireTime"), params.get("endContractExpireTime"));
        lqw.between(params.get("beginExitTime") != null && params.get("endExitTime") != null,
            HrmEmployeeRoster::getExitTime ,params.get("beginExitTime"), params.get("endExitTime"));
        return lqw;
    }

    /**
     * 新增员工花名册
     *
     * @param bo 员工花名册
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrmEmployeeRosterBo bo) {
        HrmEmployeeRoster add = MapstructUtils.convert(bo, HrmEmployeeRoster.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改员工花名册
     *
     * @param bo 员工花名册
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrmEmployeeRosterBo bo) {
        HrmEmployeeRoster update = MapstructUtils.convert(bo, HrmEmployeeRoster.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrmEmployeeRoster entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工花名册信息
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
