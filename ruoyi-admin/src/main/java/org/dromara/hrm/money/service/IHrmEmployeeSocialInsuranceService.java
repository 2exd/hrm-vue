package org.dromara.hrm.money.service;

import org.dromara.hrm.money.domain.vo.HrmEmployeeSocialInsuranceVo;
import org.dromara.hrm.money.domain.bo.HrmEmployeeSocialInsuranceBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工五险一金及专项附加扣除Service接口
 *
 * @author 2exd
 * @date 2026-03-15
 */
public interface IHrmEmployeeSocialInsuranceService {

    /**
     * 查询员工五险一金及专项附加扣除
     *
     * @param id 主键
     * @return 员工五险一金及专项附加扣除
     */
    HrmEmployeeSocialInsuranceVo queryById(Long id);

    /**
     * 分页查询员工五险一金及专项附加扣除列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工五险一金及专项附加扣除分页列表
     */
    TableDataInfo<HrmEmployeeSocialInsuranceVo> queryPageList(HrmEmployeeSocialInsuranceBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工五险一金及专项附加扣除列表
     *
     * @param bo 查询条件
     * @return 员工五险一金及专项附加扣除列表
     */
    List<HrmEmployeeSocialInsuranceVo> queryList(HrmEmployeeSocialInsuranceBo bo);

    /**
     * 新增员工五险一金及专项附加扣除
     *
     * @param bo 员工五险一金及专项附加扣除
     * @return 是否新增成功
     */
    Boolean insertByBo(HrmEmployeeSocialInsuranceBo bo);

    /**
     * 修改员工五险一金及专项附加扣除
     *
     * @param bo 员工五险一金及专项附加扣除
     * @return 是否修改成功
     */
    Boolean updateByBo(HrmEmployeeSocialInsuranceBo bo);

    /**
     * 校验并批量删除员工五险一金及专项附加扣除信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
