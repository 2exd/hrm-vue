package org.dromara.hrm.roster.service.impl;

import jakarta.annotation.Resource;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hrm.oss.domain.vo.FileOssVo;
import org.dromara.hrm.oss.domain.vo.HrmOssRelationVo;
import org.dromara.system.domain.SysOss;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.dromara.system.service.impl.SysOssServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dromara.hrm.roster.domain.bo.HrmEmployeeRosterBo;
import org.dromara.hrm.roster.domain.vo.HrmEmployeeRosterVo;
import org.dromara.hrm.roster.domain.HrmEmployeeRoster;
import org.dromara.hrm.roster.mapper.HrmEmployeeRosterMapper;
import org.dromara.hrm.roster.service.IHrmEmployeeRosterService;
import org.dromara.hrm.oss.domain.HrmOssRelation;
import org.dromara.hrm.oss.service.IHrmOssRelationService;
import org.dromara.hrm.oss.domain.bo.HrmOssRelationBo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    // 注入OSS关联表服务
    private final IHrmOssRelationService hrmOssRelationService;

    @Resource
    private final ISysOssService sysOssService;

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

        List<HrmEmployeeRosterVo> records = result.getRecords();
        if (records.isEmpty()) {
            return TableDataInfo.build(result);
        }

        // 1. 批量查询所有员工的OSS关联信息（避免N+1）
        List<Long> employeeIds = records.stream()
            .map(HrmEmployeeRosterVo::getId)
            .collect(Collectors.toList());

        // 2. 一次性查询所有关联记录
        List<HrmOssRelationVo> allRelations = hrmOssRelationService
            .queryListByBusinessIds(employeeIds, "EMPLOYEE");

        if (allRelations.isEmpty()) {
            return TableDataInfo.build(result);
        }

        // 3. 批量查询所有OSS文件信息
        List<Long> ossIds = allRelations.stream()
            .map(HrmOssRelationVo::getOssId)
            .distinct()
            .collect(Collectors.toList());

        Map<Long, SysOssVo> ossMap = sysOssService.listByIds(ossIds).stream()
            .collect(Collectors.toMap(SysOssVo::getOssId, Function.identity()));

        // 4. 按员工ID分组组装数据
        Map<Long, List<HrmOssRelationVo>> relationMap = allRelations.stream()
            .collect(Collectors.groupingBy(HrmOssRelationVo::getBusinessId));

        // 5. 填充到VO中
        for (HrmEmployeeRosterVo vo : records) {
            List<HrmOssRelationVo> relations = relationMap.get(vo.getId());
            if (relations != null) {
                List<FileOssVo> files = relations.stream()
                    .map(relation -> {
                        FileOssVo fileVo = new FileOssVo();
                        SysOssVo ossVo = ossMap.get(relation.getOssId());
                        if (ossVo != null) {
                            BeanUtils.copyProperties(ossVo, fileVo);
                        }
                        fileVo.setId(relation.getId());
                        fileVo.setBusinessId(vo.getId());
                        fileVo.setBusinessType("EMPLOYEE");
                        return fileVo;
                    })
                    .collect(Collectors.toList());
                vo.setFiles(files);
            }
        }


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
     * 新增员工花名册（含OSS文件关联）
     *
     * @param bo 员工花名册
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务保证：员工和OSS关联要么都成功，要么都回滚
    public Boolean insertByBo(HrmEmployeeRosterBo bo) {
        HrmEmployeeRoster add = MapstructUtils.convert(bo, HrmEmployeeRoster.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            if (add != null) {
                bo.setId(add.getId());
            }
            // 处理OSS文件关联
            saveOssRelation(add.getId(), bo.getFile());
        }
        return flag;
    }

    /**
     * 修改员工花名册（含OSS文件关联）
     *
     * @param bo 员工花名册
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(HrmEmployeeRosterBo bo) {
        HrmEmployeeRoster update = MapstructUtils.convert(bo, HrmEmployeeRoster.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            // 先删除该员工原有所有OSS关联记录
            deleteOssRelationByEmployeeId(bo.getId());
            // 重新插入新的OSS关联记录
            saveOssRelation(bo.getId(), bo.getFile());
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrmEmployeeRoster entity){
        //TODO 做一些数据校验,如唯一约束（身份证号、用户ID）
    }

    /**
     * 校验并批量删除员工花名册信息（含OSS关联）
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        // 先删除OSS关联记录
        ids.forEach(this::deleteOssRelationByEmployeeId);
        // 再删除员工记录
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 保存员工与OSS文件的关联关系
     * @param employeeId 员工ID
     * @param ossIds OSS文件ID列表
     */
    private void saveOssRelation(Long employeeId, List<Long> ossIds) {
        if (employeeId == null || ossIds == null || ossIds.isEmpty()) {
            return;
        }
        // 批量构建OSS关联对象
        List<HrmOssRelationBo> relationBos = ossIds.stream()
            .filter(ossId -> ossId != null) // 过滤空的OSS ID
            .map(ossId -> {
                HrmOssRelationBo bo = new HrmOssRelationBo();
                bo.setBusinessType("EMPLOYEE"); // 业务类型：员工花名册
                bo.setBusinessId(employeeId);
                bo.setOssId(ossId); // OSS文件ID
                return bo;
            }).collect(Collectors.toList());
        // 批量插入关联记录
        for (HrmOssRelationBo relationBo : relationBos) {
            hrmOssRelationService.insertByBo(relationBo);
        }
    }

    /**
     * 根据员工ID删除OSS关联记录
     * @param employeeId 员工ID
     */
    private void deleteOssRelationByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return;
        }
        // 构建查询条件：业务类型为员工 + 业务ID为员工ID
        HrmOssRelationBo bo = new HrmOssRelationBo();
        bo.setBusinessType("EMPLOYEE");
        bo.setBusinessId(employeeId);
        // 查询该员工所有OSS关联记录
        List<HrmOssRelationVo> relationVos = hrmOssRelationService.queryList(bo);
        if (!relationVos.isEmpty()) {
            // 提取主键ID并批量删除
            List<Long> relationIds = relationVos.stream()
                .map(HrmOssRelationVo::getId)
                .collect(Collectors.toList());
            hrmOssRelationService.deleteWithValidByIds(relationIds, false);
        }
    }
}
