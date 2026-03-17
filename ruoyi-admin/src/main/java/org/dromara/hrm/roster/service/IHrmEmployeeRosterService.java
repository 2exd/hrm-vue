package org.dromara.hrm.roster.service;

import org.dromara.hrm.roster.domain.vo.HrmEmployeeRosterVo;
import org.dromara.hrm.roster.domain.bo.HrmEmployeeRosterBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工花名册Service接口
 *
 * @author Lion Li
 * @date 2026-03-10
 */
public interface IHrmEmployeeRosterService {

    /**
     * 查询员工花名册
     *
     * @param id 主键
     * @return 员工花名册
     */
    HrmEmployeeRosterVo queryById(Long id);

    /**
     * 分页查询员工花名册列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工花名册分页列表
     */
    TableDataInfo<HrmEmployeeRosterVo> queryPageList(HrmEmployeeRosterBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工花名册列表
     *
     * @param bo 查询条件
     * @return 员工花名册列表
     */
    List<HrmEmployeeRosterVo> queryList(HrmEmployeeRosterBo bo);

    /**
     * 新增员工花名册
     *
     * @param bo 员工花名册
     * @return 是否新增成功
     */
    Boolean insertByBo(HrmEmployeeRosterBo bo);

    /**
     * 修改员工花名册
     *
     * @param bo 员工花名册
     * @return 是否修改成功
     */
    Boolean updateByBo(HrmEmployeeRosterBo bo);

    /**
     * 校验并批量删除员工花名册信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
