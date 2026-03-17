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
import org.dromara.hrm.money.domain.bo.HrmEmployeeSalaryBo;
import org.dromara.hrm.money.domain.vo.EmployeeSalaryDetailVo;
import org.dromara.hrm.money.domain.vo.HrmEmployeeSalaryVo;
import org.dromara.hrm.money.domain.vo.HrmEmployeeSocialInsuranceVo;
import org.dromara.hrm.money.domain.HrmEmployeeSalary;
import org.dromara.hrm.money.domain.HrmEmployeeSocialInsurance;
import org.dromara.hrm.money.mapper.HrmEmployeeSalaryMapper;
import org.dromara.hrm.money.mapper.HrmEmployeeSocialInsuranceMapper;
import org.dromara.hrm.money.service.IHrmEmployeeSalaryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工薪资Service业务层处理
 *
 * @author 2exd
 * @date 2026-03-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrmEmployeeSalaryServiceImpl implements IHrmEmployeeSalaryService {

    private final HrmEmployeeSalaryMapper baseMapper;
    private final HrmEmployeeSocialInsuranceMapper socialInsuranceMapper;

    /**
     * 查询员工薪资
     *
     * @param id 主键
     * @return 员工薪资
     */
    @Override
    public HrmEmployeeSalaryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询员工薪资列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工薪资分页列表
     */
    @Override
    public TableDataInfo<HrmEmployeeSalaryVo> queryPageList(HrmEmployeeSalaryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrmEmployeeSalary> lqw = buildQueryWrapper(bo);
        Page<HrmEmployeeSalaryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工薪资列表
     *
     * @param bo 查询条件
     * @return 员工薪资列表
     */
    @Override
    public List<HrmEmployeeSalaryVo> queryList(HrmEmployeeSalaryBo bo) {
        LambdaQueryWrapper<HrmEmployeeSalary> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrmEmployeeSalary> buildQueryWrapper(HrmEmployeeSalaryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrmEmployeeSalary> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrmEmployeeSalary::getId);
        lqw.eq(bo.getUserId() != null, HrmEmployeeSalary::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getEmployeeName()), HrmEmployeeSalary::getEmployeeName, bo.getEmployeeName());
        lqw.like(StringUtils.isNotBlank(bo.getDeptName()), HrmEmployeeSalary::getDeptName, bo.getDeptName());
        lqw.between(params.get("beginSalaryMonth") != null && params.get("endSalaryMonth") != null,
            HrmEmployeeSalary::getSalaryMonth ,params.get("beginSalaryMonth"), params.get("endSalaryMonth"));
        return lqw;
    }

    /**
     * 新增员工薪资
     *
     * @param bo 员工薪资
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrmEmployeeSalaryBo bo) {
        HrmEmployeeSalary add = MapstructUtils.convert(bo, HrmEmployeeSalary.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改员工薪资
     *
     * @param bo 员工薪资
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrmEmployeeSalaryBo bo) {
        HrmEmployeeSalary update = MapstructUtils.convert(bo, HrmEmployeeSalary.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrmEmployeeSalary entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工薪资信息
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

    /**
     * 查询员工薪资和保险详情
     *
     * @param userId     员工ID
     * @param beginMonth 开始月份（YYYYMM）
     * @param endMonth   结束月份（YYYYMM）
     * @return 员工薪资和保险详情
     */
    @Override
    public EmployeeSalaryDetailVo querySalaryDetail(Long userId, String beginMonth, String endMonth) {
        EmployeeSalaryDetailVo detailVo = new EmployeeSalaryDetailVo();
        detailVo.setUserId(userId);
        detailVo.setBeginMonth(beginMonth);
        detailVo.setEndMonth(endMonth);

        // 查询薪资列表
        HrmEmployeeSalaryBo salaryBo = new HrmEmployeeSalaryBo();
        salaryBo.setUserId(userId);
        Map<String, Object> salaryParams = new HashMap<>();
        salaryParams.put("beginSalaryMonth", beginMonth);
        salaryParams.put("endSalaryMonth", endMonth);
        salaryBo.setParams(salaryParams);
        List<HrmEmployeeSalaryVo> salaryList = this.queryList(salaryBo);
        detailVo.setSalaryList(salaryList);

        // 获取员工姓名
        if (!salaryList.isEmpty()) {
            detailVo.setEmployeeName(salaryList.get(0).getEmployeeName());
        }

        // 查询五险一金列表
        LambdaQueryWrapper<HrmEmployeeSocialInsurance> insuranceLqw = Wrappers.lambdaQuery();
        insuranceLqw.eq(HrmEmployeeSocialInsurance::getUserId, userId);
        insuranceLqw.between(HrmEmployeeSocialInsurance::getInsuranceMonth, beginMonth, endMonth);
        insuranceLqw.orderByAsc(HrmEmployeeSocialInsurance::getInsuranceMonth);
        List<HrmEmployeeSocialInsuranceVo> insuranceList = socialInsuranceMapper.selectVoList(insuranceLqw);
        detailVo.setInsuranceList(insuranceList);

        // 如果从薪资中没取到姓名，尝试从保险中取
        if (detailVo.getEmployeeName() == null && !insuranceList.isEmpty()) {
            detailVo.setEmployeeName(insuranceList.get(0).getEmployeeName());
        }

        return detailVo;
    }
}
